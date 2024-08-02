package com.learnjetpackcompose.animaldetectionandroid.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.learnjetpackcompose.animaldetectionandroid.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ClassifierHelperImage(
    val threshold: Float = 0.1f,
    val maxResult: Int = 1,
    val modelName: String = "animal_classification_model.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupClassifierImage()
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResult(result: List<Classifications>?)
    }

    private fun setupClassifierImage() {
        val optionBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResult)
        val baseOptionBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionBuilder.setBaseOptions(baseOptionBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionBuilder.build()
            )
        } catch (e: IllegalAccessException) {
            classifierListener?.onError(context.getString(R.string.gagal_mengidentifikasi_gambar))
            Log.e(TAG, e.message.toString())
        }
    }

    @Suppress("DEPRECATION")
    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupClassifierImage()
        }
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
            val result = imageClassifier?.classify(tensorImage)
            classifierListener?.onResult(result)
        }
    }

    companion object {
        private const val TAG = "ClassifierHelperImage"
    }
}