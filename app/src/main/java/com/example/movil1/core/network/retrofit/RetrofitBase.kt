package com.example.movil1.core.network.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBase {



    // Propiedad perezosa para Retrofit básico
    private val baseRetrofit: Retrofit by lazy {
        buildRetrofit()
    }

    fun buildRetrofit(client: OkHttpClient? = null): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client ?: OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideRetrofit(): Retrofit {
        return baseRetrofit
    }
}