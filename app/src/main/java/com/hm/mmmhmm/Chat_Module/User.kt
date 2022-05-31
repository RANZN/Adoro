package com.hm.mmmhmm.Chat_Module


class User {
    var userId: String? = ""

    var userName: String? = ""

    var email: String? = ""

    var isOnline: Boolean = false

    var lastMessage: String? = ""

    var time: String = ""

    var hasUnread:Boolean=false

    var id:String=""

    var profile:String=""
    override fun toString(): String {
        return "User(userId=$userId, userName=$userName, profile='$profile')"
    }


}