package com.capstone.swalokal.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.swalokal.api.response.DataItem
import com.capstone.swalokal.api.response.PredictItem
import com.capstone.swalokal.api.response.PredictionResponse
import com.capstone.swalokal.api.retrofit.ApiService
import com.capstone.swalokal.dummy.DummyResponse
import com.capstone.swalokal.dummy.createDummyResponse
import com.capstone.swalokal.dummy.getDummyJsonResponse
import com.capstone.swalokal.parseDummyResponse
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

    fun uploadPhoto(photo: File){
        val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestPhotoFile
        )

        val uploadImageRequest = apiService.uploadPhoto(imageMultipart)
        uploadImageRequest.enqueue(object : Callback<PredictionResponse>  {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>,
            ) {

                if (response.isSuccessful) {

                    val responseBody = response.body()
                    val predictItem = responseBody?.data

                    Log.d("Repo", "Berhasil upload photo\n result $predictItem")
                    Result.Success(predictItem)



                } else {
                    Log.d("Repo", "Gagal upload story : ${ response.message()}")
                    Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Result.Error(t.message ?: "Gagal upload photo")
            }
        })

    }

    // INI DUMMY
    fun uploadPhotoDummy(photo: File, callback: (Result<List<PredictItem>>) -> Unit) {
        // Simulasi pengunggahan foto ke server
        // Gunakan callback untuk mengembalikan hasil prediksi dummy
        val dummyResponse = parseDummyResponse(getDummyJsonResponse())
        callback(Result.Success(dummyResponse.data))
    }
    fun uploadPhotoChat(photo: File, callback: (Result<List<PredictItem>>) -> Unit) {
        val requestPhotoFile = photo.asRequestBody("image/jpg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestPhotoFile
        )

        val uploadImageRequest = apiService.uploadPhoto(imageMultipart)
        uploadImageRequest.enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val predictItems = responseBody?.data

                    Log.d("Repo", "Berhasil upload photo\n result $predictItems")
                    if (predictItems != null) {
                        callback(Result.Success(predictItems))
                    } else {
                        callback(Result.Error("Empty prediction result"))
                    }
                } else {
                    Log.d("Repo", "Gagal upload story : ${response.message()}")
                    callback(Result.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                callback(Result.Error(t.message ?: "Gagal upload photo"))
            }
        })
    }


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