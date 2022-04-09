package com.hm.mmmhmm.models

class RequestWithdrawalMoney internal constructor(
    var accountNumber: String?,
    var amount: Int?,
    var bankName: String?,
    var ifseCode: String?,
    var name: String?,
    var panCardNumber: String?,
    var paymentStatus: String?,
    var userId: String?,
    var username: String?
)