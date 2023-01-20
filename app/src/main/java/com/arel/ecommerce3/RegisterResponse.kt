package com.arel.ecommerce3

data class RegisterResponse(
    val success: Success? = null
)

data class Success(
    val message: String? = null,
    val status: Int? = null
)

data class RegisterResponseFail(
    val error: Error? = null
)

data class Error(
    val message: String? = null,
    val status: Int? = null
)

