package com.capstone.swalokal


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.swalokal.api.SwalokalRepository
import com.capstone.swalokal.ui.ProductImage.ProductImageViewModel
import com.capstone.swalokal.ui.Search.SearchViewModel

class ViewModelFactory(private val repository: SwalokalRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            // repo
            modelClass.isAssignableFrom(ProductImageViewModel::class.java) -> {
                ProductImageViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}
