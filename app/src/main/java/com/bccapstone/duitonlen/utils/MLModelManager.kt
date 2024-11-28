package com.bccapstone.duitonlen.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.tasks.await
import org.tensorflow.lite.Interpreter

class MLModelManager(private val context: Context, private val modelName: String) {
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
    fun processImage(bitmap: Bitmap): Float {
        val model = customModel ?: run {
            Log.e("MLModelManager", "Model not downloaded yet")
            return 0f
        }

        try {
            val inputArray = preprocessImage(bitmap)
            val interpreter = model.file?.let { Interpreter(it) }
            val outputArray = Array(1) { FloatArray(1) } // Adjust size based on the model

            interpreter?.run(inputArray, outputArray)
            interpreter?.close()

            // Log the output for debugging
            Log.d("MLModelManager", "Model output: ${outputArray[0][0]}")

            // Return the prediction if human detected (threshold can be adjusted)
            return outputArray[0][0]
        } catch (e: Exception) {
            Log.e("MLModelManager", "Error processing image: ${e.message}")
            return 0f
        }
    }


    // normalize the image converts pixel values to float values from 0 to 1 range.
    private fun preprocessImage(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val result = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }

        for (x in 0 until 224) {
            for (y in 0 until 224) {
                val pixel = bitmap.getPixel(x, y)
                result[0][x][y][0] = Color.red(pixel) / 255f
                result[0][x][y][1] = Color.green(pixel) / 255f
                result[0][x][y][2] = Color.blue(pixel) / 255f
            }
        }
        return result
    }

}

