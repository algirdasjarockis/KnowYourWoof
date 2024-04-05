package com.ajrock.knowyourwoof.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "scores", indices = [Index("date")])
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val correctAnswers: Int,
    val totalAttempts: Int,
    val date: String
)
