package com.sz.randombark.feature.data.model

import com.google.gson.annotations.SerializedName

data class RandomDogImageReply(
    @SerializedName("message")
    val imageUrl: String,

    @SerializedName("status")
    val status: String
)
