package com.bccapstone.duitonlen.helper

import android.content.Context
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.InterpreterApi
import java.io.IOException
import java.nio.ByteBuffer

/*
class PredictionHelper(
    private val modelName: String,
    val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
    private val onDownloadSuccess: () -> Unit
){
    var interpreter: InterpreterApi? = null
    private var isGPUSupported: Boolean = false

    init {
        TfLiteGpu.isGpuDelegateAvailable(context).onSuccessTask { isSupported ->
            val optionsBuilder = TfLiteInitializationOptions.builder()
            if (isSupported) {
                optionsBuilder.setEnableGpuDelegateSupport(true)
                isGPUSupported = true
            }
            TfLite.initialize(context, optionsBuilder.build())
        }.addOnSuccessListener {
            downloadModel()
        }.addOnFailureListener {
            onError(it.message ?: "Failed to initialize TFLite")
        }
    }

    @Synchronized
    private fun downloadModel() {
        // Download model from server
        val conditions = CustomModelDownloadConditions.Builder()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel(modelName, DownloadType.LATEST_MODEL, conditions)
            .addOnSuccessListener { model: CustomModel ->
                try {
                    // download success and init a prediciton helper
                    onDownloadSuccess()
                    initializeInterpreter(model)
                } catch (e: IOException) {
                    onError(e.message.toString())
                }
            }
            .addOnFailureListener {
                onError(it.message ?: "Failed to download model, please check your internet connection")
            }
    }

    private fun initializeInterpreter(model: Any) {
        interpreter?.close()
        try {
            val options = InterpreterApi.Options()
                .setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
                .addDelegateFactory(GpuDelegateFactory())
            if (model is ByteBuffer) {
                interpreter = InterpreterApi.create(model, options)
            } else if (model is CustomModel) {
                model.file?.let {
                    interpreter = InterpreterApi.create(it, options)
                }
            }
        } catch (e: Exception) {
            onError(e.message ?: "Failed to initialize interpreter")
            Log.e(TAG, e.message.toString())
        }
    }

    companion object {
        private const val TAG = "PredictionHelper"
    }


}

 */