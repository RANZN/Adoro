package com.hm.mmmhmm.models

class FollowRequest internal constructor(
    var myDetail: MyDetail?,
    var personWhomeImFollowingData: PersonWhomeImFollowingData?
)

class MyDetail internal constructor(
    var _id: String?,
    var name: String?,
    var username: String?
)

data class PersonWhomeImFollowingData(
    var _id: String?,
    var name: String?,
    var username: String?
)