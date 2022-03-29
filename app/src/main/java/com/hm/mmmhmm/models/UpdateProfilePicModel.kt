package com.hm.mmmhmm.models


import com.google.gson.annotations.SerializedName

data class UpdateProfilePicModel(
    @SerializedName("data")
    val `data`: List<String>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)