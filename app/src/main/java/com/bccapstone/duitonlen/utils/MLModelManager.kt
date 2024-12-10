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

    // Download the model from Firebase
    suspend fun downloadModel(): Boolean {
        return try {
            val conditions = CustomModelDownloadConditions.Builder()
                .build()

            customModel = modelDownloader.getModel(
                modelName, // Your model name in Firebase
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

        // when modelName is "all-pos" the output array is 1,5 shapes and input range 0 to 1. but if "front-facing-ubeid" the output array is 1,2 shapes and input range -1 to 1.

        try {
            // when modelName is "all-pos" the output array is 1,5 shapes and input range 0 to 1. but if "front-facing-ubeid" the output array is 1,2 shapes and input range -1 to 1.
            val inputArray: Array<Array<Array<FloatArray>>> = when (modelName) {
                "all-pos" -> {
                    preprocessImageOne(bitmap)
                }
                "front-facing-ubeid" -> {
                    preprocessImageTwo(bitmap)
                }
                else -> {
                    Log.e("MLModelManager", "Model not found")
                    return floatArrayOf(0f, 0f)
                }
            }
            val outputArray: Array<FloatArray> = when (modelName) {
                "all-pos" -> {
                    Array(1) { FloatArray(5) }
                }
                "front-facing-ubeid" -> {
                    Array(1) { FloatArray(1) }
                }
                else -> {
                    Log.e("MLModelManager", "Model not found")
                    return floatArrayOf(0f, 0f)
                }
            }

            val interpreter = model.file?.let { Interpreter(it) }

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

    // Normalize the image to the range [0, 1]
    private fun preprocessImageOne(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val result = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }

        for (x in 0 until 224) {
            for (y in 0 until 224) {
                val pixel = bitmap.getPixel(x, y)
                result[0][x][y][0] = Color.red(pixel) / 255.0f
                result[0][x][y][1] = Color.green(pixel) / 255.0f
                result[0][x][y][2] = Color.blue(pixel) / 255.0f
            }
        }
        return result
    }

    // Normalize the image to the range [-1, 1]
    private fun preprocessImageTwo(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val result = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }

        for (x in 0 until 224) {
            for (y in 0 until 224) {
                val pixel = bitmap.getPixel(x, y)
                result[0][x][y][0] = (Color.red(pixel) / 255.0f) * 2 - 1
                result[0][x][y][1] = (Color.green(pixel) / 255.0f) * 2 - 1
                result[0][x][y][2] = (Color.blue(pixel) / 255.0f) * 2 - 1
            }
        }
        return result
    }

}