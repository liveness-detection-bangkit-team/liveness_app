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
    private lateinit var cameraManager: CameraManager

    private val _isHumanDetected = mutableStateOf(false)
    val isHumanDetected: State<Boolean> = _isHumanDetected

    private val _onHumanDetected = mutableFloatStateOf(0f)
    val onHumanDetected: State<Float> = _onHumanDetected

    private var detectionStartTime: Long? = null

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
        modelName: String
    ) {
        viewModelScope.launch {
            mlModelManager = MLModelManager(context, modelName)

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
                    _onHumanDetected.floatValue = humanDetected

                    if (humanDetected >= 0.35f) {
                        if (detectionStartTime == null) {
                            detectionStartTime = System.currentTimeMillis()
                        } else {
                            val detectionTime = System.currentTimeMillis() - detectionStartTime!!
                            _countdownTime.intValue = (3000 - detectionTime).toInt()
                            if (detectionTime >= 3000 && countdownTime.value <= 0) {
                                _isHumanDetected.value = true
                                onHumanDetected()
                            }
                        }
                    } else{
                        detectionStartTime = null
                    }

                }
            }

            cameraManager.startCamera(previewView)
        }
    }
}

