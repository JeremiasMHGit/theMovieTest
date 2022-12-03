package com.example.themovietest.model.remote.network

import android.content.Context
import com.example.themovietest.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptorConnection(var context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val orginalHttpURL = original.url

        val url = orginalHttpURL.newBuilder()
            .addQueryParameter("api_key", BuildConfig.MOVIES_API_KEY)
            .build()

        val request = original
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)

    }
}