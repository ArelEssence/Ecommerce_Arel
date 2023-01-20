package com.arel.ecommerce3.model


data class ResponseLogin(
    val success: Success? = null
)

data class Success(
    val access_token: String? = null,
    val refresh_token: String? = null,
    val data_user: DataUser? = null,
    val message: String? = null,
    val status: Int? = null
)

data class DataUser(
    val image: String? = null,
    val path: String? = null,
    val gender: Int? = null,
    val phone: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val email: String? = null
)

data class LoginResponseFail(
    val error: ErrorData? = null
)

data class ErrorData(
    val message: String? = null,
    val status: Int? = null
)