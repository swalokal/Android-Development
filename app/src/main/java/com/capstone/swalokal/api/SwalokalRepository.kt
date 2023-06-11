package com.capstone.swalokal.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.swalokal.api.response.DataItem
import com.capstone.swalokal.api.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SwalokalRepository private constructor(private val apiService: ApiService) {

    // search
    fun getSearchProduct(query: String): LiveData<Result<List<DataItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchProduct(query)
            val products = response.detail
            Result.Success(products)
        } catch (e: Exception) {
            Log.d("UserRepository", "getSearchUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }

    }

    // tanpa call
//    suspend fun uploadPhotoTanpaCall(photo: File): PredictionResponse {
//        com.capstone.swalokal.api.Result.Loading
//        return try {
//            val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "photo",
//                photo.name,
//                requestPhotoFile
//            )
//            val response = apiService.uploadPhotoTanpaCall(imageMultipart)
//
//            if (response.prediction.isNullOrEmpty()) {
//                Log.d("repo", "response null or empty")
//                Result.Error("Error")
//                PredictionResponse(emptyList())
//
//            } else {
//                Log.d("repo", "data : ${response.prediction}")
//                Result.Success("Success")
//                PredictionResponse(response.prediction)
//            }
//        } catch (e: Exception) {
//            Log.e("repo", "Terjadi kesalahan saat predict: ${e.message}")
//            Result.Error(e.message ?: "Gagal predict gambar")
//            PredictionResponse(emptyList())
//        }
//    }

//    fun uploadPhoto(photo: File) {
//        Log.d("Repo", "repo dijalankan")
//        val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
//        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//            "photo",
//            photo.name,
//            requestPhotoFile
//        )
//        val uploadImageRequest = apiService.uploadPhoto(imageMultipart)
//
//        uploadImageRequest.enqueue(object : Callback<PredictionResponse> {
//            override fun onResponse(
//                call: Call<PredictionResponse>,
//                response: Response<PredictionResponse>,
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("Repo", "is succesful")
//                    val responseBody = response.body()
//                    val predictionResult = responseBody?.prediction
//
//                    if (predictionResult.isNullOrEmpty()) {
//                        Log.d("Repo", "list nya kosong")
//
//                    } else {
//                        Log.d("Repo", "Berhasil upload photo")
//                        Log.d("Repo", "${responseBody.prediction}")
//                        Result.Success("Success")
//                    }
//                } else {
//                    Log.d("Repo", "Gagal upload story : ${response.message()}")
//                    Result.Error("Error")
//                }
//            }
//
//            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
//                Result.Error(t.message ?: "Gagal upload photo")
//            }
//        })
//    }

    companion object {
        @Volatile
        private var instance: SwalokalRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): SwalokalRepository =
            instance ?: synchronized(this) {
                instance ?: SwalokalRepository(apiService)
            }.also { instance = it }
    }
}