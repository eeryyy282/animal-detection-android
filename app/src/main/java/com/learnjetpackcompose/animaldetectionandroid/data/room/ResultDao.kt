package com.learnjetpackcompose.animaldetectionandroid.data.room

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import com.learnjetpackcompose.animaldetectionandroid.data.entity.ResultEntity

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveResult(resultEntity: ResultEntity): Long

    @Query("SELECT * FROM ResultEntity WHERE image_uri = :imageUri LIMIT 1")
    fun getResultByUri(imageUri: String): ResultEntity?

    @Query("SELECT * FROM ResultEntity")
    fun showResult(): LiveData<List<ResultEntity>>

    @Delete
    fun deleteResult(resultEntity: ResultEntity)
}

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}