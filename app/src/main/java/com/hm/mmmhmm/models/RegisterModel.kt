package com.hm.mmmhmm.models

data class RegisterModel(
    val OK: OKSignupResponse
)

data class OKSignupResponse(
    val _createdDate: String,
    val _id: String,
    val _updatedDate: String,
    val email: String,
    val profile: String,
    val token: String,
    val name: String,
    val number: Long,
    val username: String,
    val referCode: String,)