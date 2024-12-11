package com.bccapstone.duitonlen.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.tasks.await
import org.tensorflow.lite.Interpreter

class MLModelManager(private val modelName: String) {
    private val modelDownloader = FirebaseModelDownloader.getInstance()
    private var customModel: CustomModel? = null // Store the downloaded model

    companion object {
        private const val INPUT_DIMENSION = 224
    }

    // Download the model from Firebase
    suspend fun downloadModel(): Boolean {
        return try {
            val conditions = CustomModelDownloadConditions.Builder().build()
            customModel = modelDownloader.getModel(
                modelName,
                DownloadType.LATEST_MODEL,
                conditions
            ).await()
            customModel != null
        } catch (e: Exception) {
            Log.e("MLModelManager", "Error downloading model: ${e.message}")
            false
        }
    }

    // Process the image using the downloaded model
    fun processImage(bitmap: Bitmap): FloatArray {
        val model = customModel ?: run {
            Log.e("MLModelManager", "Model not downloaded yet")
            return floatArrayOf(0f, 0f)
        }

        try {
            val inputArray = preprocessImage(bitmap)
            logMinMaxValues(inputArray)
            val outputArray = createOutputArray()

            val interpreter = model.file?.let { Interpreter(it) }

            // Log the shapes for debugging
            Log.d("MLModelManager", "Model input shape: ${interpreter?.getInputTensor(0)?.shape()?.contentToString()}")

            interpreter?.run(inputArray, outputArray)
            interpreter?.close()

            // Log the output for debugging
            Log.d("MLModelManager", "Model output: ${outputArray[0].contentToString()}")

            // Return the prediction probabilities
            return outputArray[0]
        } catch (e: Exception) {
            Log.e("MLModelManager", "Error processing image: ${e.message}")
            return floatArrayOf(0f, 0f)
        }
    }

    // Preprocess the image based on the model name
    private fun preprocessImage(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val result = Array(1) { Array(INPUT_DIMENSION) { Array(INPUT_DIMENSION) { FloatArray(3) } } }
        val (normalizeRange, offset) = when (modelName) {
            // "all-pos" and "spoof-detect" model expects input in the range of 0-1
            "all-pos", "spoof-detect" -> Pair(1.0f, 0.0f)
            // "all-pos-quant" model expects input in the range of -1 to 1
            "all-pos-quant" -> Pair(2.0f, -1.0f)
            else -> {
                Log.e("MLModelManager", "Model not found")
                return result
            }
        }

        for (x in 0 until INPUT_DIMENSION) {
            for (y in 0 until INPUT_DIMENSION) {
                val pixel = bitmap.getPixel(x, y)
                result[0][x][y][0] = (Color.red(pixel) / 255.0f) * normalizeRange + offset
                result[0][x][y][1] = (Color.green(pixel) / 255.0f) * normalizeRange + offset
                result[0][x][y][2] = (Color.blue(pixel) / 255.0f) * normalizeRange + offset
            }
        }

        return result
    }

    // Create the output array based on the model name
    private fun createOutputArray(): Array<FloatArray> {
        return when (modelName) {
            "all-pos", "all-pos-quant" -> Array(1) { FloatArray(5) }
            "spoof-detect" -> Array(1) { FloatArray(2) }
            else -> {
                Log.e("MLModelManager", "Model not found")
                arrayOf()
            }
        }
    }

    private fun logMinMaxValues(inputArray: Array<Array<Array<FloatArray>>>) {
        var min = Float.MAX_VALUE
        var max = Float.MIN_VALUE

        for (x in inputArray[0].indices) {
            for (y in inputArray[0][x].indices) {
                for (c in inputArray[0][x][y].indices) {
                    val value = inputArray[0][x][y][c]
                    if (value < min) min = value
                    if (value > max) max = value
                }
            }
        }

        Log.d("MLModelManager", "Input range: Min = $min, Max = $max")
    }
}