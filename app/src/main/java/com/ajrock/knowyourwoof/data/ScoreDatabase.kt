package com.ajrock.knowyourwoof.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScoreEntity::class], version = 1, exportSchema = false)
abstract class ScoreDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var Instance: ScoreDatabase? = null

        fun getDatabase(context: Context): ScoreDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ScoreDatabase::class.java, "score_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}