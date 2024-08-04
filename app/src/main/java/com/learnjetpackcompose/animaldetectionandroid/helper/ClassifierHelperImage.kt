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
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.NumberFormat

class ClassifierHelperImage(
    val threshold: Float = 0.1f,
    val maxResult: Int = 1,
    val modelName: String = "animal_classification_model.tflite",
    val labelsFileName: String = "labels_mobilenet_quant_v1_224.txt",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null
    private var labels: List<String> = emptyList()

    init {
        setupClassifierImage()
        loadLabels()
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResult(result: List<String>?)
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

    private fun loadLabels() {
        try {
            val reader = BufferedReader(InputStreamReader(context.assets.open(labelsFileName)))
            labels = reader.readLines()
            reader.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error membaca label", e)
            classifierListener?.onError("Error membaca label: ${e.message}")
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
            val labeledResults = result?.flatMap { it.categories }?.map { category ->
                val label = labels.getOrNull(category.index) ?: "Unknown"
                val scorePercentage = NumberFormat.getPercentInstance().format(category.score)
                "$label\n$scorePercentage"
            }
            classifierListener?.onResult(labeledResults)
        }
    }

    companion object {
        private const val TAG = "ClassifierHelperImage"
    }
}
