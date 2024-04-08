package com.ajrock.knowyourwoof.data.repository

import com.ajrock.knowyourwoof.data.api.IDogCeoApiService
import com.ajrock.knowyourwoof.data.api.apiRequestWrapper
import com.ajrock.knowyourwoof.model.BreedModel

internal class DogRepository(private val doggoApi: IDogCeoApiService) : IDogRepository {
    override suspend fun fetchAllBreeds(): List<BreedModel> {
        val response = apiRequestWrapper { doggoApi.fetchAllBreeds() }
        val list = mutableListOf<BreedModel>()

        if (!response.isError) {
            response.data?.breeds?.forEach { (breed, subBreeds) ->
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
            apiRequestWrapper { doggoApi.fetchImageByBreed(breedModel.breed) }
        } else {
            apiRequestWrapper { doggoApi.fetchImageBySubBreed(breedModel.breed, breedModel.subBreed) }
        }

        if (!response.isError) {
            return response.data?.imagePath
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