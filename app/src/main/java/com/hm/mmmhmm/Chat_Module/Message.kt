package com.hm.mmmhmm.Chat_Module

class Message {
    var message: String = ""
    var receiver: String = ""
    var sender: String = ""
    var time: Long = 0L
    var type: String = ""
    var seen: Boolean = false
    override fun toString(): String {
        return "Message(message='$message', receiver='$receiver', sender='$sender', time=$time, type='$type', seen=$seen)"
    }


}