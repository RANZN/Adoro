package com.hm.mmmhmm.models

data class UpdateProfileRequest(
    val _id: String,
    val accountNumber: String,
    val adoroCoins: Int,
    val adoroShield: Int,
    val bankName: String,
    val completeAddress: CompleteAddress,
    val dob: String,
    val email: String,
    val gender: String,
    val ifseCode: String,
    val name: String,
    val number: Long,
    val panCardNumber: String,
    val username: String
)
