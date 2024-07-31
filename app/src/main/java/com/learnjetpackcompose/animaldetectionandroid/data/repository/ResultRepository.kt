package com.learnjetpackcompose.animaldetectionandroid.data.repository

import androidx.lifecycle.LiveData
import com.learnjetpackcompose.animaldetectionandroid.data.entity.ResultEntity
import com.learnjetpackcompose.animaldetectionandroid.data.room.ResultDao
import com.learnjetpackcompose.animaldetectionandroid.utils.AppExecutors

class ResultRepository(
    private val resultDao: ResultDao,
    private val appExecutors: AppExecutors
) {
    fun saveResult(resultEntity: ResultEntity) {
        appExecutors.diskIO.execute {
            resultDao.saveResult(resultEntity)
        }
    }

    fun removeResult(resultEntity: ResultEntity) {
        appExecutors.diskIO.execute {
            resultDao.deleteResult(resultEntity)
        }
    }

    fun showResult(): LiveData<List<ResultEntity>> {
        return resultDao.showResult()
    }

    fun getResultByUri(imageUri: String, callback: (ResultEntity?) -> Unit) {
        appExecutors.diskIO.execute {
            val resultEntity = resultDao.getResultByUri(imageUri)
            appExecutors.mainThread.execute {
                callback(resultEntity)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ResultRepository? = null

        fun getInstance(
            resultDao: ResultDao,
            appExecutors: AppExecutors
        ): ResultRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ResultRepository(resultDao, appExecutors)
            }.also { INSTANCE = it }

    }

}