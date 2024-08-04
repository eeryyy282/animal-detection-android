package com.learnjetpackcompose.animaldetectionandroid.view.home

import androidx.lifecycle.ViewModel
import com.learnjetpackcompose.animaldetectionandroid.data.entity.ResultEntity
import com.learnjetpackcompose.animaldetectionandroid.data.repository.ResultRepository

class HomeViewModel(
    private val resultRepository: ResultRepository
) : ViewModel() {

    fun getResult() = resultRepository.showResult()

    fun removeResult(resultEntity: ResultEntity) {
        resultRepository.removeResult(resultEntity)
    }
}