package com.hm.mmmhmm.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DefaultResponse(
    val message: String="",
    val status: Int
):Serializable

