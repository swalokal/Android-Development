package com.capstone.swalokal.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.swalokal.api.response.DataItem
import com.capstone.swalokal.api.response.PredictItem
import com.capstone.swalokal.api.response.PredictionResponse
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
    fun uploadPhotoTanpaCall(photo: File): PredictionResponse {
        Result.Loading
        return try {
            val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                photo.name,
                requestPhotoFile
            )
            val response = apiService.uploadPhotoTanpaCall(imageMultipart)
            val respData = response.data

            Log.d("repo", "data : $respData")
            Result.Success(respData)
            PredictionResponse(respData)

        } catch (e: Exception) {
            Log.e("repo", "Terjadi kesalahan saat predict: ${e.message}")
            Result.Error(e.message ?: "Gagal predict gambar")
            PredictionResponse(emptyList())
        }
    }

//    fun uploadPhoto(photo: File): com.capstone.swalokal.api.Result<List<PredictItem>> {
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
//                    Log.d("Repo", "Berhasil upload photo")
//                    val responseBody = response.body()
//                    val predictionResult = responseBody?.data
//
//                    if (predictionResult.isNullOrEmpty()) {
//                        Log.d("Repo", "$predictionResult")
//                        Log.d("Repo", "list nya kosong")
//
//                    } else {
//
//                        Log.d("Repo", "$predictionResult")
//                        Result.Success(predictionResult)
//                    }
//                } else {
//                    Log.d("Repo", "Gagal upload story : ${response.message()}")
//                    Result.Error(response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
//                Result.Error(t.message ?: "Gagal upload photo")
//            }
//        })
//
//
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