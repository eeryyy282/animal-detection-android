package com.learnjetpackcompose.animaldetectionandroid.view.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.learnjetpackcompose.animaldetectionandroid.R
import com.learnjetpackcompose.animaldetectionandroid.databinding.ActivityResultBinding
import com.learnjetpackcompose.animaldetectionandroid.helper.DateHelper
import com.learnjetpackcompose.animaldetectionandroid.helper.showToast
import com.learnjetpackcompose.animaldetectionandroid.view.home.HomeActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var resultViewModel: ResultViewModel
    private var imageUri: Uri? = null
    private var result: String? = null
    private var timeDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factoryResult: ResultViewModelFactory = ResultViewModelFactory.getInstance(application)
        resultViewModel = ViewModelProvider(this, factoryResult)[ResultViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupResult()
        setupAction()

    }

    @Suppress("DEPRECATION")
    private fun setupResult() {
        imageUri = intent.getParcelableExtra(EXTRA_IMAGE)
        result = intent.getStringExtra(EXTRA_RESULT)
        timeDate = DateHelper.getCurrentDate()

        binding.ivResult.setImageURI(imageUri)
        binding.tvResult.text = result
    }

    private fun setupAction() {
        binding.buttonBackHome.setOnClickListener {
            val intent = Intent(this@ResultActivity, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }

        binding.buttonSaveResult.setOnClickListener {
            if (imageUri != null && result != null) {
                resultViewModel.saveResult(result = result, time = timeDate, image = imageUri)
                showToast(
                    this@ResultActivity,
                    getString(R.string.berhasil_menyimpan_hasil_prediksi)
                )
            } else {
                showToast(
                    this@ResultActivity,
                    getString(R.string.tidak_bisa_menyimpan_terjadi_kesalahan_menyimpan_data)
                )
            }
        }
    }


    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT = "extra_result"
    }
}