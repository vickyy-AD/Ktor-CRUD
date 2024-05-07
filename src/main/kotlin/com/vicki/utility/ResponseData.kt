package com.vicki.utility

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    val status: Boolean? = null,
    val message: String? = null,
    val data: T? = null
)