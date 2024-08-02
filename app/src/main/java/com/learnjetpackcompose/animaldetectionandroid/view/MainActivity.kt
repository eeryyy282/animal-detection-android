package com.learnjetpackcompose.animaldetectionandroid.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.learnjetpackcompose.animaldetectionandroid.R
import com.learnjetpackcompose.animaldetectionandroid.databinding.ActivityMainBinding
import com.learnjetpackcompose.animaldetectionandroid.helper.ClassifierHelperImage
import com.learnjetpackcompose.animaldetectionandroid.helper.ShowToast
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var classifierHelperImage: ClassifierHelperImage

    private var imageUri: Uri? = null
    private var resultModel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            imageUri = UCrop.getOutput(data!!)
            imageShow()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.d("Crop Error", "showImage: $cropError")
            ShowToast(
                context = this@MainActivity,
                message = getString(R.string.tidak_dapat_menggunakan_fungsi_crop_untuk_saat_ini)
            )
        }

    }


    private fun setupAction() {
        binding.buttonSelectPicture.setOnClickListener {
            openGallery()
        }
        binding.buttonNextMainActivity.setOnClickListener {
            detectionImage()
        }
    }

    private fun openGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            ucrop(uri)
            imageShow()
        } else {
            Log.d("Photo Picker", getString(R.string.tidak_ditemukan_foto_coba_lagi))
        }
    }

    private fun ucrop(uri: Uri) {
        val fileName = "${UUID.randomUUID()}.jpg"
        val filePath = Uri.fromFile(File(filesDir, fileName))

        val option = UCrop.Options()
        option.setCompressionQuality(100)

        UCrop.of(uri, filePath)
            .withOptions(option)
            .withAspectRatio(0f, 0f)
            .useSourceImageAspectRatio()
            .withMaxResultSize(3000, 3000)
            .start(this)
    }

    private fun detectionImage() {
        if (imageUri != null) {
            classifierHelperImage = ClassifierHelperImage(
                context = this,
                classifierListener = object : ClassifierHelperImage.ClassifierListener {
                    override fun onError(error: String) {
                        ShowToast(context = this@MainActivity, message = error)
                    }

                    override fun onResult(result: List<Classifications>?) {
                        result?.let { it ->
                            println(it)
                            val category = it[0].categories.sortedByDescending { it?.score }
                            val displayResult = category.joinToString("\n") {
                                "${it.label}\n" + NumberFormat.getPercentInstance()
                                    .format(it.score).trim()
                            }
                            resultModel = displayResult
                            toTheResultActivity()
                        }
                    }

                }
            )
            imageUri?.let { classifierHelperImage.classifyStaticImage(it) }
        } else {
            ShowToast(this@MainActivity, getString(R.string.tidak_ada_gambar_yang_dipilih))
        }
    }

    private fun toTheResultActivity() {
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        intent.putExtra(EXTRA_IMAGE, imageUri)
        intent.putExtra(EXTRA_RESULT, resultModel)
        startActivity(intent)
    }

    private fun imageShow() {
        imageUri?.let {
            Log.d("URI Image", "showImage: $it")
            binding.ivPhotoAnalyze.setImageURI(it)
        }
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT = "extra_result"
    }
}