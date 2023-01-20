package com.arel.ecommerce3

data class RefreshTokenResponse(
    val success : SuccessRefreshToken
)

data class SuccessRefreshToken(
    val status : Int,
    val access_token : String,
    val refresh_token : String,
    val message : String
)

