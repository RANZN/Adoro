package com.hm.mmmhmm.models

import com.hm.mmmhmm.helper.SessionManager

data class NotificationPublish(
    val to: String = "/topics/${SessionManager.getUserName()!!.lowercase()}",
    val data: NotificationData
)

data class NotificationData(
    val type: String,
    val count: String
)