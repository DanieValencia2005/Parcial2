package com.ud.parcial2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
 //private const val BASE_URL = "http://192.168.1.21:8000/"
    //private const val BASE_URL = "https://ngrok.com/docs/errors/err_ngrok_4018/"
    //private const val BASE_URL = "http:localhost:8000/"
   private const val BASE_URL = "http://10.0.2.2:3003/"

    val api: SubastaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SubastaApiService::class.java)
    }
}
