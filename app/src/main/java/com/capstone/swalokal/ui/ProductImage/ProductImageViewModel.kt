package com.capstone.swalokal.ui.ProductImage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.swalokal.data.api.SwalokalRepository
import kotlinx.coroutines.launch
import com.capstone.swalokal.data.api.Result
import com.capstone.swalokal.data.api.response.PredictItem
import com.capstone.swalokal.data.api.response.PredictionResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProductImageViewModel(private val swalokalRepository: SwalokalRepository) : ViewModel() {

    // get data
    fun getData() = swalokalRepository.getData()

    // upload and make prediction
    private val _uploadResult = MutableLiveData<Result<List<PredictItem>>>()
    val uploadResult: LiveData<Result<List<PredictItem>>> get() = _uploadResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun uploadPhoto(photo: File) {
        viewModelScope.launch {
            _loading.value = true
            val result = swalokalRepository.uploadPhoto(photo)
            _uploadResult.value = result
            _loading.value = false
        }
        Log.d("VM", uploadResult.toString())
    }

    // INI DUMMY
    fun uploadPhotoDummy(photo: File): LiveData<Result<List<PredictItem>>> {
        val resultLiveData = MutableLiveData<Result<List<PredictItem>>>()

        swalokalRepository.uploadPhotoDummy(photo) { result ->
            resultLiveData.postValue(result)
        }
        return resultLiveData
    }
}