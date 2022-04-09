package com.hm.mmmhmm.models


import com.google.gson.annotations.SerializedName

data class UserDetailModel(
    @SerializedName("data")
    var data: UserData,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Int
)

data class UserData(
    @SerializedName("address")
    var address: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("dob")
    var dob: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("email_verified_at")
    var emailVerifiedAt: Any,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("identity_number")
    var identityNumber: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("profile_picture")
    var profilePicture: String,
    @SerializedName("role")
    var role: String,
    @SerializedName("sexual_orientation")
    var sexualOrientation: String,
    @SerializedName("updated_at")
    var updatedAt: String
)
