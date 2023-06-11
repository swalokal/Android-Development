package com.capstone.swalokal.ui.ProductImage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.swalokal.api.SwalokalRepository
import kotlinx.coroutines.launch
import com.capstone.swalokal.api.Result
import java.io.File

class ProductImageViewModel(private val swalokalRepository: SwalokalRepository) : ViewModel() {

    private var _uploadResult = MutableLiveData<Result<Unit>>()
    val uploadResult: LiveData<Result<Unit>> get() = _uploadResult

    // upload and make prediction
//    fun uploadPhoto(photo: File) {
//        Log.d("Repo", "view model dijalankan")
//        viewModelScope.launch {
//            Result.Loading
//            try {
//                val result = swalokalRepository.uploadPhotoTanpaCall(photo)
//                Result.Success(result)
//
//
//            } catch (e: Exception) {
//                _uploadResult.value = Result.Error(e.message ?: "Failed to upload story")
//            }
//        }
//    }

}