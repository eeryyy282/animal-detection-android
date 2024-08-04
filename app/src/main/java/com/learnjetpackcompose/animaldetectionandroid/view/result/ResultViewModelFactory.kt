package com.learnjetpackcompose.animaldetectionandroid.view.result

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnjetpackcompose.animaldetectionandroid.data.repository.ResultRepository
import com.learnjetpackcompose.animaldetectionandroid.di.Injection

class ResultViewModelFactory private constructor(
    private val resultRepository: ResultRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(resultRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak dapat ditemukan")
    }

    companion object {
        @Volatile
        private var INSTANCE: ResultViewModelFactory? = null

        fun getInstance(context: Context): ResultViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ResultViewModelFactory(
                    Injection.resultRepository(context)
                )
            }.also { INSTANCE = it }
    }
}