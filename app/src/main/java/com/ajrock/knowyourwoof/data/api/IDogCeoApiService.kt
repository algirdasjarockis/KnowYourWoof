package com.ajrock.knowyourwoof.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class RawBreedResponse (
    @SerializedName("message")
    val breeds : HashMap<String, List<String>>? = null,
    val status : String? = null
)

data class RawBreedWithRandomImageResponse(
    @SerializedName("message")
    val imagePath : String? = null,
    val status : String? = null
)

interface IDogCeoApiService {
    @GET("breeds/list/all")
    suspend fun fetchAllBreeds(): Response<RawBreedResponse>

    @GET("breed/{breed}/images/random")
    suspend fun fetchImageByBreed(@Path("breed") breed: String): Response<RawBreedWithRandomImageResponse>

    @GET("breed/{breed}/{subBreed}/images/random")
    suspend fun fetchImageBySubBreed(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String
    ): Response<RawBreedWithRandomImageResponse>
}