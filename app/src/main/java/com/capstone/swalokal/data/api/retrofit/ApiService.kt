package com.capstone.swalokal.data.api.retrofit

import com.capstone.swalokal.data.api.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("all")
    fun getData(): PredictionResponse

    // prediction
    @Multipart
    @POST("make-predictions")
    suspend fun uploadPhoto(@Part photo: MultipartBody.Part): Response<PredictionResponse>


}