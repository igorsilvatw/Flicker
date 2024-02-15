package com.flicker.cvs.feature.flimage.domain

import com.flicker.cvs.feature.flimage.data.FLImageRepository
import com.flicker.cvs.feature.flimage.data.FLImageResponse
import retrofit2.Response

class FLImageUseCase(private val repository: FLImageRepository = FLImageRepository.instantiate()) {

    suspend fun getImages(tag: String): Response<FLImageResponse> {
        val query = replaceSpabeByComma(tag)
        return repository.getImages(query)
    }

    private fun replaceSpabeByComma(tag: String) = tag.replace(" ", ",")

    companion object {
        fun instantiate(): FLImageUseCase = FLImageUseCase()
    }
}
