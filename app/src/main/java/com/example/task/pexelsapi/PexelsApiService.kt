package com.example.task.pexelsapi

import com.example.task.fragments.HomeFragment
import com.example.task.pexelsapi.dataclases.CollectionMediaResponse
import com.example.task.pexelsapi.dataclases.FeaturedCollectionsResponse
import com.example.task.pexelsapi.dataclases.PexelsResponse
import com.example.task.pexelsapi.dataclases.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PexelsApiService {
    @GET("curated")
    fun getCuratedImages(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<PexelsResponse>

    @GET("collections/featured")
    fun getFeaturedCollections(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<FeaturedCollectionsResponse>

    @GET("collections/{id}")
    fun getCollectionMedia(
        @Header("Authorization") apiKey: String,
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<CollectionMediaResponse>

    @GET("photos/{id}")
    fun getPhoto(
        @Path("id") photoId: String,
        @Header("Authorization") apiKey: String
    ): Call<Photo>
}