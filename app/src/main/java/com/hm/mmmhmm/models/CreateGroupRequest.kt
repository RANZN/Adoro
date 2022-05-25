package com.hm.mmmhmm.models

class CreateGroupRequest internal constructor(
    var category: String?,
    var description: String?,
    var groupName: String?,
    var groupProfile: String?,
    val memberData: Array<Int> = emptyArray(),
    var privacy: String?,
    var ownerId: String?,
    val requestedMemberData: List<Any> = emptyList(),


    )
