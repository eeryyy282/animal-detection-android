package com.learnjetpackcompose.animaldetectionandroid.view.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnjetpackcompose.animaldetectionandroid.data.repository.ResultRepository
import com.learnjetpackcompose.animaldetectionandroid.di.Injection

class HomeViewModelFactory private constructor(
    private val resultRepository: ResultRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(resultRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak ditemukan " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTACE: HomeViewModelFactory? = null

        fun getInstace(
            context: Context
        ): HomeViewModelFactory = INSTACE ?: synchronized(this) {
            INSTACE ?: HomeViewModelFactory(Injection.resultRepository(context))
        }.also { INSTACE = it }
    }
}