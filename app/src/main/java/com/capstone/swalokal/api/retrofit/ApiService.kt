package com.capstone.swalokal.api.retrofit

import com.capstone.swalokal.api.response.SearchResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // search product
    @GET("all/{query}")
    suspend fun searchProduct(
        @Path("query") productName: String
    ): SearchResponse

//    // prediction
//    @Multipart
//    @POST("make-predictions")
//    fun uploadPhoto(
//        @Part file: MultipartBody.Part,
//    ): Call<PredictionResponse>
//
//    // tanpa call
//    // prediction
//    @Multipart
//    @POST("make-predictions")
//    fun uploadPhotoTanpaCall(
//        @Part file: MultipartBody.Part,
//    ): PredictionResponse
}