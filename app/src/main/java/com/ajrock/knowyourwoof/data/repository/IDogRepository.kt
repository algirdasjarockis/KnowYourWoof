package com.ajrock.knowyourwoof.data.repository

import com.ajrock.knowyourwoof.model.BreedModel

interface IDogRepository {
    suspend fun fetchAllBreeds(): List<BreedModel>

    suspend fun fetchImageByBreed(breedModel: BreedModel): String?
}