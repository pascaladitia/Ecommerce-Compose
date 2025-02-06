package com.pascal.ecommercecompose.ui.screen.verified

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.ui.theme.AppTheme

@Composable
fun VerifiedScreen(
    modifier: Modifier = Modifier,
    onNavBack: () -> Unit
) {
    val context = LocalContext.current
    val pref = PreferencesLogin.getLoginResponse(context)
    var detectedSmile by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (detectedSmile) {
            Toast.makeText(LocalContext.current, "Deteksi Senyum Berhasil!", Toast.LENGTH_SHORT).show()

            PreferencesLogin.setLoginResponse(context, pref?.apply { isVerified = true })
            onNavBack()
        }

        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(LocalContext.current) { smileDetected ->
                detectedSmile = smileDetected
            }
        }
    }
}

@Composable
fun CameraPreview(context: Context, onSmileDetected: (Boolean) -> Unit) {
    val faceDetector = remember {
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setMinFaceSize(0.1f)
                .build()
        )
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(cameraProviderFuture) {
        cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    val preview = androidx.camera.core.Preview.Builder().build()
                    val imageAnalysis = ImageAnalysis.Builder().build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        analyzeImage(imageProxy, faceDetector, onSmileDetected)
                    }

                    cameraProvider.bindToLifecycle(
                        context as LifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("CameraX", "Error initializing camera: ${e.message}")
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    AndroidView(
        factory = {
            previewView.apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
fun analyzeImage(
    imageProxy: ImageProxy,
    faceDetector: FaceDetector,
    onSmileDetected: (Boolean) -> Unit
) {
    val image = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
    faceDetector.process(image)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                val face = faces.first()
                val smileProbability = face.smilingProbability ?: 0f
                Log.d("SmileDetection", "Senyum terdeteksi: $smileProbability")
                val smileDetected = smileProbability > 0.3f
                onSmileDetected(smileDetected)
            } else {
                Log.d("SmileDetection", "Tidak ada wajah yang terdeteksi")
            }
            imageProxy.close()
        }
        .addOnFailureListener { exception ->
            Log.e("SmileDetection", "Deteksi wajah gagal", exception)
            imageProxy.close()
        }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        VerifiedScreen { }
    }
}