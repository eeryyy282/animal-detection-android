package com.learnjetpackcompose.animaldetectionandroid.data.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "image_uri")
    var imageUri: Uri,

    @ColumnInfo(name = "timestamps")
    var timeStamps: String,

    @ColumnInfo(name = "predicted_class")
    var predictedClass: String,

    @ColumnInfo(name = "confidence_score")
    var confidenceScore: String

)