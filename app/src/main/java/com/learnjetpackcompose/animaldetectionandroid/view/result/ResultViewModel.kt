package com.learnjetpackcompose.animaldetectionandroid.view.result

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learnjetpackcompose.animaldetectionandroid.data.entity.ResultEntity
import com.learnjetpackcompose.animaldetectionandroid.data.repository.ResultRepository

class ResultViewModel(
    private val resultRepository: ResultRepository
) : ViewModel() {

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String> = _snackBarText

    fun saveResult(result: String?, time: String?, image: Uri?) {
        val imageToString = image.toString()

        resultRepository.getResultByUri(imageToString) {
            if (it == null) {
                val resultEntity = image?.let { it1 ->
                    time?.let { it2 ->
                        result?.let { it3 ->
                            ResultEntity(
                                imageUri = it1,
                                timeStamps = it2,
                                result = it3
                            )
                        }
                    }
                }
                resultEntity?.let { it1 -> resultRepository.saveResult(it1) }
                _snackBarText.value = "Berhasil menyimpan hasil"
            } else {
                _snackBarText.value = "Gagal menyimpan hasil"
            }
        }
    }
}