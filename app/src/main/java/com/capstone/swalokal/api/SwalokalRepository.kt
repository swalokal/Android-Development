package com.capstone.swalokal.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.swalokal.api.response.PredictItem
import com.capstone.swalokal.api.retrofit.ApiService
import com.capstone.swalokal.dummy.getDummyJsonResponse
import com.capstone.swalokal.parseDummyResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
class SwalokalRepository private constructor(private val apiService: ApiService) {

    // get data
    fun getData(): LiveData<Result<List<PredictItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getData()
            val products = response.detail?.data
            if (products != null){
                val filteredProducts = products.filterNotNull()
                emit(Result.Success(filteredProducts))
            } else {
                emit(Result.Error("Data is null"))
            }

        } catch (e: Exception) {
            Log.d("UserRepository", "getData: ${e.message.toString()} ")
            Result.Error(e.message.toString())
        }
    }

    fun uploadPhoto(photo: File): Result<List<PredictItem>> {
        val requestFile = photo.asRequestBody(MultipartBody.FORM)
        val filePart = MultipartBody.Part.createFormData("file", photo.name, requestFile)
        return try {
            val response = apiService.makePredictions(filePart)
            val responseData = response.detail.data
            if (responseData.isNotEmpty() && response.detail.eror == false) {
                Log.d("repo", "upl success : $responseData ")
                Result.Success(responseData)
            } else {
                Log.d("repo", "upl err: null/isempty/error ")
                Result.Error("Failed to make predictions")
            }

        } catch (e: Exception) {
            Log.d("repo", "upl err: exception")
            Result.Error(e.message ?: "Unknown error")
        }
    }

    // tanpa call
//    fun uploadPhotoTanpaCall(photo: File): PredictionResponse {
//        Result.Loading
//        return try {
//            val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "photo",
//                photo.name,
//                requestPhotoFile
//            )
//            val response = apiService.uploadPhotoTanpaCall(imageMultipart)
//            val respData = response.detail
//
//            Log.d("repo", "data : $respData")
//            Result.Success(respData)
//            PredictionResponse(respData)
//
//        } catch (e: Exception) {
//            Log.e("repo", "Terjadi kesalahan saat predict: ${e.message}")
//            Result.Error(e.message ?: "Gagal predict gambar")
//            PredictionResponse()
//        }
//    }












//fun uploadPhoto(photo: File){
//        val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
//        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//            "photo",
//            photo.name,
//            requestPhotoFile
//        )
//
//        val uploadImageRequest = apiService.uploadPhoto(imageMultipart)
//        uploadImageRequest.enqueue(object : Callback<PredictionResponse>  {
//            override fun onResponse(
//                call: Call<PredictionResponse>,
//                response: Response<PredictionResponse>,
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    val predictItem = responseBody?.detail?.data
//
//                    Log.d("Repo", "Berhasil upload photo\n result $predictItem")
//                    Result.Success(predictItem)
//                } else {
//                    Log.d("Repo", "Gagal upload story : ${ response.message()}")
//                    Result.Error(response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
//                Result.Error(t.message ?: "Gagal upload photo")
//            }
//        })
//
//    }

    // INI DUMMY
    fun uploadPhotoDummy(photo: File, callback: (Result<List<PredictItem>>) -> Unit) {
        // Simulasi pengunggahan foto ke server
        // Gunakan callback untuk mengembalikan hasil prediksi dummy
        val dummyResponse = parseDummyResponse(getDummyJsonResponse())
        callback(Result.Success(dummyResponse.data))
    }

//    fun uploadPhotoChat(photo: File, callback: (Result<List<PredictItem>>) -> Unit) {
//        val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
//        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//            "photo",
//            photo.name,
//            requestPhotoFile
//        )
//
//        val uploadImageRequest = apiService.uploadPhoto(imageMultipart)
//        uploadImageRequest.enqueue(object : Callback<PredictionResponse> {
//            override fun onResponse(
//                call: Call<PredictionResponse>,
//                response: Response<PredictionResponse>,
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    val predictItems = responseBody?.data
//
//                    Log.d("Repo", "Berhasil upload photo\n result $predictItems")
//                    if (predictItems != null) {
//                        callback(Result.Success(predictItems))
//                    } else {
//                        callback(Result.Error("Empty prediction result"))
//                    }
//                } else {
//                    Log.d("Repo", "Gagal upload story : ${response.message()}")
//                    callback(Result.Error(response.message()))
//                }
//            }
//
//            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
//                callback(Result.Error(t.message ?: "Gagal upload photo"))
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