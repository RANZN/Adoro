package com.hm.mmmhmm.models


import com.google.gson.annotations.SerializedName

data class UpdateUserInfoModel(
    @SerializedName("data")
    val data: UserData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

