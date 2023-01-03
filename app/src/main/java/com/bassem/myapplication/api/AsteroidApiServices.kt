package com.bassem.myapplication.api

import com.bassem.myapplication.Constants.BASE_URL
import com.bassem.myapplication.model.PictureOfDay
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/2/2023.
 */
class AsteroidApiService {

    interface AsteroidApiServices {
        @GET("neo/rest/v1/feed")
        suspend fun getAsteroids(@Query("start_date") startDate: String, @Query("end_date") endDate: String, @Query("api_key") apiKey: String): String

        @GET("planetary/apod")
        suspend fun getPlanetaryApod(@Query("api_key") apiKey: String): PictureOfDay

    }

    object AsteroidApi {
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


        val retrofitService: AsteroidApiServices by lazy { retrofit.create(AsteroidApiServices::class.java) }

    }
}