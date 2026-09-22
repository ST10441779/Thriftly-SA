package com.example.thriftlysa

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    /*
     * IMPORTANT:
     *
     * Replace this URL with your hosted API URL.
     *
     * It must end with /
     */
    private const val BASE_URL =
        "https://YOUR-DOMAIN.com/"

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}