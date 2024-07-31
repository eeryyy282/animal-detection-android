package com.learnjetpackcompose.animaldetectionandroid.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.learnjetpackcompose.animaldetectionandroid.data.entity.ResultEntity

@Database(entities = [ResultEntity::class], version = 1)
@TypeConverters(UriConverter::class)
abstract class ResultDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile
        private var INSTANCE: ResultDatabase? = null

        @JvmStatic
        fun getInstace(context: Context): ResultDatabase {
            if (INSTANCE == null) {
                synchronized(ResultDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ResultDatabase::class.java, "result_database"
                    )
                        .build()
                }
            }
            return INSTANCE as ResultDatabase
        }
    }
}