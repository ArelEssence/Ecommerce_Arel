package com.arel.ecommerce3

data class ModelPreference(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val gender: Int,
    val path: String,
    val token: String,
    val refreshToken: String,
    val isLogin: Boolean,
    val isLanguage: Int
)
