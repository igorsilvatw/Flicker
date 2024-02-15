package com.flicker.cvs.platform

import com.flicker.cvs.feature.flimage.data.FLImageResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


const val HOST = "https://api.flickr.com/"
const val PATH = "services/feeds/photos_public.gne"
//const val URL_SERViCE = HOST + PATH

const val FORMAT = "json"
const val NO_JSON_CALLBACK = "1"


interface RetrofitService {

    @GET(value = PATH)
    suspend fun getFlickerImages(

        @Query("format") format: String = FORMAT,
        @Query("nojsoncallback") noJsonCallback: String = NO_JSON_CALLBACK,
        @Query("tags") query: String
    ): Response<FLImageResponse>

    companion object {
        val INSTANCE by lazy {
            Retrofit.Builder().baseUrl(HOST)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .create()
                    )
                )
                .client(
                    OkHttpClient.Builder().connectTimeout(
                        5,
                        TimeUnit.SECONDS
                    ).addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.HEADERS
                    }).build()
                )
                .build().create(RetrofitService::class.java)
        }

    }

}
