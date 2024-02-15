package com.flicker.cvs.feature.flimage.data

import com.flicker.cvs.platform.RetrofitService

class FLImageRepository(private val retrofitService: RetrofitService = RetrofitService.INSTANCE) {

    suspend fun getImages(query: String) = retrofitService.getFlickerImages(query = query)

    companion object {
        fun instantiate() : FLImageRepository = FLImageRepository()
    }
}
