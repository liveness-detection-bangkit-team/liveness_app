package com.bccapstone.duitonlen.ui.screen.liveness

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
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


    // is direction detected
    private val _isDirectionDetected = mutableStateOf(false)
    val isDirectionDetected: State<Boolean> = _isDirectionDetected

    private val _faceDirectionResult = mutableFloatStateOf(0f)
    val faceDirectionResult: State<Float> = _faceDirectionResult

    // face direction
    private val _faceDirection = mutableStateOf("")
    val faceDirection: State<String> = _faceDirection

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
                // Process the image using the ML model, and update the UI, if human detected (threshold can be adjusted)
                // will return true, if the human face is detected for 3 seconds
                viewModelScope.launch {
                    val humanDetected = mlModelManager.processImage(bitmap)

                    /*
                    * get the highest probability from the model output
                    * with according labels [0: "background_train", 1: "front_facing",2: "left", 3: "right", 4: "up"]
                    * with that ignore the first
                     */
                    val highestProbability = humanDetected.sliceArray(1..4).maxOrNull() ?: 0f
                    val faceDirection = when (highestProbability) {
                        humanDetected[1] -> "front"
                        humanDetected[2] -> "left"
                        humanDetected[3] -> "right"
                        humanDetected[4] -> "up"
                        else -> "others"
                    }
                    _faceDirection.value = faceDirection
                    _faceDirectionResult.floatValue = highestProbability


                    val currentTime = System.currentTimeMillis()

                    if (faceDirection == headMotion) {
                        if (detectionStartTime == null) {
                            detectionStartTime = currentTime
                        } else {
                            val detectionTime = currentTime - detectionStartTime!!
                            _countdownTime.intValue = (3000 - detectionTime).toInt()
                            lastCorrectPositionTime = currentTime

                            if (detectionTime >= 3000 && countdownTime.value <= 0) {
                                _isDirectionDetected.value = true
                                onHumanDetected()
                            }
                        }
                    } else {
                        // Stop the countdown if the face direction changes
                        detectionStartTime = null
                        lastCorrectPositionTime = null
                    }
                }

            }

            cameraManager!!.startCamera(previewView)
        }
    }

    // Release camera resources
    fun releaseCamera() {
        cameraManager?.unbindAll()
    }

    // Re-initialize camera resources
    fun reinitializeCamera(previewView: PreviewView) {
        cameraManager?.startCamera(previewView)
    }
}

