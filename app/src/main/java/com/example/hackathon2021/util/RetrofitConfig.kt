package com.example.hackathon2021.util

import com.example.hackathon2021.dao.AccountRetrofit
import com.example.hackathon2021.dao.PostRetrofit
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    private const val BASE_URL = "http://172.20.10.2:8080/"
    private fun retrofit(): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().addInterceptor(Interceptor()).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val accountRetrofit: AccountRetrofit by lazy {
        retrofit().create(AccountRetrofit::class.java)
    }
    val postRetrofit: PostRetrofit by lazy {
        retrofit().create(PostRetrofit::class.java)
    }


    class Interceptor : okhttp3.Interceptor {
        override fun intercept(chain: okhttp3.Interceptor.Chain): Response {
            var req = chain.request().newBuilder().build()
            if (mApplication.prefs.token != null) {
                req =
                    chain.request().newBuilder().addHeader(
                        "Authorization",
                        "Bearer " + mApplication.prefs.token!!
                    ).build()
            }
            return chain.proceed(req)
        }

    }
}