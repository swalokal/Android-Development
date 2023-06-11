package com.capstone.swalokal.ui.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.swalokal.api.SwalokalRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SwalokalRepository): ViewModel() {

    fun getSearchUser(query: String) = repository.getSearchProduct(query)



}