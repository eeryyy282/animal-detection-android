package com.learnjetpackcompose.animaldetectionandroid.view.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnjetpackcompose.animaldetectionandroid.R
import com.learnjetpackcompose.animaldetectionandroid.data.entity.ResultEntity
import com.learnjetpackcompose.animaldetectionandroid.databinding.ActivityHomeBinding
import com.learnjetpackcompose.animaldetectionandroid.view.adapter.HistoryResultAdapter
import com.learnjetpackcompose.animaldetectionandroid.view.main.MainActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factoryHome: HomeViewModelFactory = HomeViewModelFactory.getInstace(this)
        homeViewModel = ViewModelProvider(this, factoryHome)[HomeViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupHistory()
        setupAction()
    }


    private fun setupHistory() {
        val resultAdapter = HistoryResultAdapter { result ->
            homeViewModel.removeResult(result)
        }

        homeViewModel.getResult().observe(this) { result ->
            binding.progressBarHistoryResult.visibility = View.GONE
            val items = arrayListOf<ResultEntity>()
            result.map {
                val item =
                    ResultEntity(
                        id = it.id,
                        imageUri = it.imageUri,
                        timeStamps = it.timeStamps,
                        result = it.result
                    )
                items.add(item)
            }
            resultAdapter.submitList(items)

            if (items.isEmpty()) {
                binding.ivAnimalMsg.visibility = View.VISIBLE
                binding.tvErrorMsg.visibility = View.VISIBLE
            } else {
                binding.ivAnimalMsg.visibility = View.GONE
                binding.tvErrorMsg.visibility = View.GONE
            }

        }

        binding.rvResult.apply {
            adapter = resultAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@HomeActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun setupAction() {
        binding.buttonStartDetection.setOnClickListener {
            intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}