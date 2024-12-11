package com.bccapstone.duitonlen.presentation.screens.liveness

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bccapstone.duitonlen.utils.CameraManager
import com.bccapstone.duitonlen.utils.MLModelManager
import kotlinx.coroutines.launch

class LivenessViewModel : ViewModel() {
    private lateinit var mlModelManager: MLModelManager
    var cameraManager: CameraManager? = null

    private var detectionStartTime: Long? = null
    private var lastCorrectPositionTime: Long? = null
    private var overallStartTime: Long? = null
    private var isCountdownActive = false

    private val GRACE_PERIOD = 1500L
    private val DETECTION_TIME = 3000L
    private val TIMEOUT = 8000L

    // model output
    private val _modelOutput = mutableStateOf("")

    private val _countdownTime = mutableIntStateOf(0)
    val countdownTime: State<Int> = _countdownTime

    private val _isModelReady = mutableStateOf(false)
    val isModelReady: State<Boolean> = _isModelReady

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Initialize the detection process
    fun initializeDetection(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onHumanDetected: () -> Unit,
        onHumanNotDetected: () -> Unit,
        modelName: String,
        headMotion: String
    ) {
        viewModelScope.launch {
            mlModelManager = MLModelManager(modelName)

            val modelDownloaded = mlModelManager.downloadModel()
            if (!modelDownloaded) {
                _errorMessage.value = "Failed to download ML model"
                return@launch
            }

            _isModelReady.value = true

            cameraManager = CameraManager(
                context,
                lifecycleOwner
            ) { bitmap ->
                viewModelScope.launch {
                    processImage(bitmap, modelName, headMotion, onHumanDetected, onHumanNotDetected)
                }
            }

            cameraManager!!.startCamera(previewView)
        }
    }

private fun processImage(
    bitmap: Bitmap,
    modelName: String,
    headMotion: String,
    onHumanDetected: () -> Unit,
    onHumanNotDetected: () -> Unit
) {
    val humanDetected = mlModelManager.processImage(bitmap)

    // Check if headMotion is "up", if it is select humanDetected[4] as highestProbability
    val highestProbability = if (headMotion == "up") {
        humanDetected[4]
    } else {
        humanDetected.maxOrNull() ?: 0f
    }

    // Only initialize modelOutput if headMotion is not "up"
    if (headMotion != "up") {
        val modelOutput = getModelOutput(modelName, highestProbability, humanDetected)

        // Log modelOutput
        Log.d("LivenessViewModel", "Model Output: $modelOutput")

        _modelOutput.value = modelOutput
    }

    val currentTime = System.currentTimeMillis()

    if (overallStartTime == null && cameraManager != null && isModelReady.value) {
        overallStartTime = currentTime
    }

    if (headMotion == "up" && highestProbability > 0.1f) {
        handleCorrectHeadMotion(currentTime, onHumanDetected)
    } else if (headMotion != "up" && _modelOutput.value == headMotion) {
        handleCorrectHeadMotion(currentTime, onHumanDetected)
    } else {
        handleIncorrectHeadMotion(currentTime, modelName)
    }

    if (!isCountdownActive && overallStartTime != null && currentTime - overallStartTime!! > TIMEOUT) {
        releaseCamera()
        onHumanNotDetected()
    }
}

    private fun getModelOutput(modelName: String, highestProbability: Float, humanDetected: FloatArray): String {
        return when (modelName) {
            "all-pos","all-pos-quant" -> {
                when (highestProbability) {
                    humanDetected[1] -> "front"
                    humanDetected[2] -> "left"
                    humanDetected[3] -> "right"
                    humanDetected[4] -> "up"
                    else -> "bg train"
                }
            }
            "spoof-detect" -> {
                when (highestProbability) {
                    humanDetected[0] -> "fake"
                    else -> "face"
                }
            }
            else -> "Invalid Model Name"
        }
    }

    private fun handleCorrectHeadMotion(currentTime: Long, onHumanDetected: () -> Unit) {
        isCountdownActive = true
        if (detectionStartTime == null) {
            detectionStartTime = currentTime
        } else {
            val detectionTime = currentTime - detectionStartTime!!
            _countdownTime.intValue = (DETECTION_TIME - detectionTime).toInt()
            lastCorrectPositionTime = currentTime

            if (detectionTime >= DETECTION_TIME && countdownTime.value <= 0) {
                releaseCamera()
                onHumanDetected()
            }
        }
    }

    private fun handleIncorrectHeadMotion(currentTime: Long, modelName: String) {
        isCountdownActive = false
        if (modelName != "spoof-detect") {
            if (lastCorrectPositionTime != null && currentTime - lastCorrectPositionTime!! <= GRACE_PERIOD) {
                detectionStartTime = currentTime - (DETECTION_TIME - countdownTime.value)
            } else {
                resetCountdown()
            }
        } else {
            resetCountdown()
        }
    }

    private fun resetCountdown() {
        detectionStartTime = null
        lastCorrectPositionTime = null
    }

    // Release camera resources
    fun releaseCamera() {
        cameraManager?.unbindAll()
        cameraManager = null
    }

    // Re-initialize camera resources
    fun reinitializeCamera(previewView: PreviewView) {
        cameraManager?.startCamera(previewView)
    }
}