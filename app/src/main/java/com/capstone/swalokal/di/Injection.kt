package com.example.storyapp.di

import android.content.Context
import com.capstone.swalokal.api.SwalokalRepository
import com.capstone.swalokal.api.retrofit.ApiConfig


object Injection {
    fun provideRepository(context: Context) : SwalokalRepository {
        val apiService = ApiConfig.getApiService()
        return SwalokalRepository.getInstance(apiService)
    }

}