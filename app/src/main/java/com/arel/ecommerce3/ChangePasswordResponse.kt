package com.arel.ecommerce3

data class ChangePasswordResponse(
    val success: SuccessChangePass? = null
)

data class SuccessChangePass(
    val message: String? = null,
    val status: Int? = null
)

data class ChangePasswordResponseFail(
    val error: ErrorDataChangePass? = null
)

data class ErrorDataChangePass(
    val message: String? = null,
    val status: Int? = null
)

