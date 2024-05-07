package com.vicki

import kotlinx.serialization.Serializable

@Serializable
data class userModel(
    val userId: Int,
    val userName: String,
    val phoneNumber: String,
)