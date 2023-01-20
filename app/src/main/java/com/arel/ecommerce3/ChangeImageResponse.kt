package com.arel.ecommerce3

data class ChangeImageResponse(
    val success: SuccessChangeImage? = null
)

data class SuccessChangeImage(
    val path: String? = null,
    val message: String? = null,
    val status: Int? = null
)

data class ChangeImageResponseFail(
    val message: String? = null
)

