package com.learnjetpackcompose.animaldetectionandroid.di

import android.content.Context
import com.learnjetpackcompose.animaldetectionandroid.data.repository.ResultRepository
import com.learnjetpackcompose.animaldetectionandroid.data.room.ResultDatabase
import com.learnjetpackcompose.animaldetectionandroid.utils.AppExecutors

object Injection {
    fun resultRepository(context: Context): ResultRepository {
        val database = ResultDatabase.getInstace(context)
        val dao = database.resultDao()
        val appExecutors = AppExecutors()
        return ResultRepository.getInstance(dao, appExecutors)

    }
}