package com.hm.mmmhmm.models

data class Item(
    val _createdDate: String?,
    val _id: String?,
    val _owner: String?,
    val _updatedDate: String?,
    val appliedNumber: Int?,
    val brandLogo: String?,
    val brandName: String?,
    val campignImage: String?,
    val comment: Int?,
    val cost: String?,
    val description: String?,
    val like: Int?,
    val shortDescription: String?,
    val timeLeft: String?,
    val email: String?,
    val name: String?,
    val number: Long?,
    val panCardNumber: String?,
    val username: String?,
    val dob: String?,
    val followerData: List<Any>?,
    val followingData: List<FollowingData>?,
    val gender: String?,
    val ifseCode: String?,
    val myGroupInfo: List<Any>?,
    val accountNumber: String?,
    val adoroCoins: Int?,
    val adoroShield: Int?,
    val bankName: String?,
    val completeAddress: CompleteAddress?,
)
