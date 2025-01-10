package com.sz.randombark.feature.data.reply

import com.google.gson.annotations.SerializedName

data class RandomDogReply(
    @SerializedName("message")
    val imageUrl: String,

    @SerializedName("status")
    val status: String
)
