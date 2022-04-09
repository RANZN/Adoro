package com.hm.mmmhmm.web_service

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.hm.mmmhmm.helper.GlobleData
import com.hm.mmmhmm.helper.SessionManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {
    //const val BASE_URL = "https://webhoodindia.wixsite.com/"
    const val BASE_URL = "https://www.marqueberry.com/"

    val gson = GsonBuilder()
        .setLenient()
        .create()

    fun getRetrofitService(context:Context): ApiInterface {
        SessionManager.init(context)
        val myClient= getClient()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(myClient)
            .build().create(ApiInterface::class.java)
    }


    private fun getClient(): OkHttpClient? {

        val headerInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val request: Request.Builder = original.newBuilder()
                .header("Accept", "application/json")
                .method(original.method(), original.body())

            if(SessionManager.getAccessToken().toString().isNotEmpty()){
                request.addHeader("Authorization", "Bearer"+" "+ SessionManager.getAccessToken().toString())
            }

            chain.proceed(request.build())
        }

        val logingInterceptor = HttpLoggingInterceptor()
        logingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
//            .addNetworkInterceptor(logingInterceptor)
//            .addNetworkInterceptor(AddHeaderInterceptor())

            .addNetworkInterceptor(headerInterceptor)
            .addNetworkInterceptor(logingInterceptor)

            .callTimeout(0, TimeUnit.MINUTES)
            .connectTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        client.connectionPool().evictAll()
        return client
    }


 /*   class AddHeaderInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
//            builder.addHeader("X-Requested-With", "XMLHttpRequest")
            builder.addHeader("Accept", "application/json")
            builder.addHeader("Authorization", "Bearer"+" "+GlobleData.ACCESS_TOKEN)
//            Log.e("access_token", accessToken)
            return chain.proceed(builder.build())
        }
    }*/
}
