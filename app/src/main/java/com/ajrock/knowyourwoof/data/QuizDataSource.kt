package com.ajrock.knowyourwoof.data

import com.ajrock.knowyourwoof.model.DogPhotoItem
import com.ajrock.knowyourwoof.model.QuizItem

// https://upload.wikimedia.org/wikipedia/commons
object QuizDataSource {
    val items = listOf(
        QuizItem(
            doggo = "Boxer",
            photo = DogPhotoItem(
                "/thumb/6/6f/Male_fawn_Boxer_undocked.jpg/1200px-Male_fawn_Boxer_undocked.jpg?20150508222146",
                "Mood210"
            )
        ),
        QuizItem(
            doggo = "Chinook",
            photo = DogPhotoItem(
                "/8/84/Mountan_Laurel_Ajax_the_Chinook_dog.jpg",
                "Chinookfan"
            )
        ),
        QuizItem(
            doggo = "Jack Russel Terrier",
            photo = DogPhotoItem(
                "/thumb/f/f1/Jack_Russell_Terrier_1.jpg/800px-Jack_Russell_Terrier_1.jpg",
                "Plank"
            )
        )
    )
}