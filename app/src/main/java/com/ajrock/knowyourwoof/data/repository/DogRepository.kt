package com.ajrock.knowyourwoof.data.repository

import com.ajrock.knowyourwoof.data.api.IDogCeoApiService
import com.ajrock.knowyourwoof.model.BreedModel

internal class DogRepository(private val doggoApi: IDogCeoApiService) : IDogRepository {
    override suspend fun fetchAllBreeds(): List<BreedModel> {
        val response = doggoApi.fetchAllBreeds()
        val list = mutableListOf<BreedModel>()

        if (response.body() != null) {
            response.body()!!.breeds?.forEach { (breed, subBreeds) ->
                if (subBreeds.isEmpty()) {
                    list.add(createBreedModel(breed, ""))
                } else {
                    subBreeds.forEach {
                        list.add(createBreedModel(breed, it))
                    }
                }
            }
        }

        return list;
    }

    override suspend fun fetchImageByBreed(breedModel: BreedModel): String? {
        val response = if (breedModel.subBreed.isNullOrEmpty()) {
            doggoApi.fetchImageByBreed(breedModel.breed)
        } else {
            doggoApi.fetchImageBySubBreed(breedModel.breed, breedModel.subBreed)
        }

        if (response.body() != null) {
            return response.body()!!.imagePath
        }

        return null
    }

    private fun createBreedModel(breed: String, subBreed: String) : BreedModel {
        val displayBreed = breed.replaceFirstChar { it.uppercase() }
        val displaySubBreed = subBreed.replaceFirstChar { it.uppercase() }

        return BreedModel(
            displayName = "$displaySubBreed $displayBreed".trim(),
            breed = breed,
            subBreed = subBreed
        )
    }
}