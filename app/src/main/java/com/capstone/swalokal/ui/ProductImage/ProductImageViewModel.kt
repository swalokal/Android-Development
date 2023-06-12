package com.capstone.swalokal.ui.ProductImage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.swalokal.api.SwalokalRepository
import kotlinx.coroutines.launch
import com.capstone.swalokal.api.Result
import com.capstone.swalokal.api.response.PredictionResponse
import java.io.File

class ProductImageViewModel(private val swalokalRepository: SwalokalRepository) : ViewModel() {

    private var _uploadResult = MutableLiveData<Result<PredictionResponse>>()
    val uploadResult: LiveData<Result<PredictionResponse>> get() = _uploadResult

    // upload and make prediction
    fun uploadPhoto(photo: File) {
        Log.d("Repo", "view model dijalankan")
        viewModelScope.launch {
            _uploadResult.value = Result.Loading
            try {
                val result = swalokalRepository.uploadPhotoTanpaCall(photo)
                _uploadResult.value =  Result.Success(result)


            } catch (e: Exception) {
                _uploadResult.value = Result.Error(e.message ?: "Failed to upload story")
            }
        }
    }

}