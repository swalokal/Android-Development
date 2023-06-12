package com.capstone.swalokal.ui.ProductImage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.swalokal.api.SwalokalRepository
import kotlinx.coroutines.launch
import com.capstone.swalokal.api.Result
import com.capstone.swalokal.api.response.PredictItem
import com.capstone.swalokal.api.response.PredictionResponse
import java.io.File

class ProductImageViewModel(private val swalokalRepository: SwalokalRepository) : ViewModel() {

    private var _uploadResult = MutableLiveData<Result<Unit>>()
    val uploadResult: LiveData<Result<Unit>> get() = _uploadResult

    private var _list = MutableLiveData<List<PredictItem>>()
    val list: LiveData<List<PredictItem>> get() = _list

    // upload and make prediction
    fun uploadPhoto(photo: File) {
        Log.d("view model", "view model dijalankan")

        viewModelScope.launch {
            _uploadResult.value = Result.Loading
            try {
                val result = swalokalRepository.uploadPhoto(photo)
                _uploadResult.value =  Result.Success(result)

                Log.d("view model", "result sukses")

            } catch (e: Exception) {
                _uploadResult.value = Result.Error(e.message ?: "Failed to upload story")
                Log.d("view model", "exception ${e.message}")
            }
        }
    }
    fun uploadPhotoTanpaCall(photo: File) {

        val result = swalokalRepository.uploadPhotoTanpaCall(photo)
        _list.value = result.data

        Log.d("view model", "view model tanpa call dijalankan\n list : $list")


//        viewModelScope.launch {
//            _uploadResult.value = Result.Loading
//            try {
//                val result = swalokalRepository.uploadPhoto(photo)
//                _uploadResult.value =  Result.Success(result)
//                Log.d("view model", "result sukses")
//
//            } catch (e: Exception) {
//                _uploadResult.value = Result.Error(e.message ?: "Failed to upload story")
//                Log.d("view model", "exception")
//            }
//        }
    }


    // chat

    fun uploadPhotoChat(photo: File): LiveData<Result<List<PredictItem>>> {
        val resultLiveData = MutableLiveData<Result<List<PredictItem>>>()

        swalokalRepository.uploadPhotoChat(photo) { result ->
            resultLiveData.postValue(result)
        }

        return resultLiveData
    }

}