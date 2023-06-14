package com.capstone.swalokal.api.retrofit

import com.capstone.swalokal.api.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("all")
    fun getData(): PredictionResponse

    // prediction
    @Multipart
    @POST("make-predictions")
    fun makePredictions(
        @Part file: MultipartBody.Part,
    ): PredictionResponse

//    // tanpa call
    @Multipart
    @POST("make-predictions")
    fun uploadPhotoTanpaCall(
        @Part file: MultipartBody.Part,
    ): PredictionResponse
}