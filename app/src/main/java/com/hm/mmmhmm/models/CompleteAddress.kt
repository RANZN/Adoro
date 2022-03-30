package com.hm.mmmhmm.models

data class CompleteAddress(
    val areaName: String,
    val city: String,
    val landmark: String,
    val state: String,
    val streetAddress: String,
    val zipCode: Int
)