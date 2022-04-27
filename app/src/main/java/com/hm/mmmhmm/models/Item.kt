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
    val comment: Object?,
    val cost: String?,
    val description: String?,
    val like: Object?,
    val shortDescription: String?,
    val timeLeft: String?,
    val email: String?,
    val name: String?,
    val number: Long?,
    val panCardNumber: String?,
    val username: String?,
    val text: String?,
    val profilePhoto: String?,
    val userName: String?,
    val dob: String?,
    val followerData: List<Any>?,
    val followingData: List<FollowingData>?,
    val gender: String?,
    val ifseCode: String?,
    val myGroupInfo: List<Object>?,
    val accountNumber: String?,
    val adoroCoins: Int?,
    val adoroShield: Int?,
    val bankName: String?,
    val completeAddress: CompleteAddress?,
    val image: String?,
    val imageLink: String?,
    val title: String?,
    val bio: String?,
    val id: String?,
    val profile: String?,
    val bannerImage: String?,
    val serialNumber: Int?,
    val category: String?,
    val groupName: String?,
    val groupProfile: String?,
    val memberData: List<Any>?,
    val privacy: String?,
    var requriementType: String?,
    var message: String?,
    var sessionId: Long?,
    var winnerDetails: List<WinnerDetail>?
)
