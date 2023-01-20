package com.arel.ecommerce3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestLogin(
    val email: String? = null,
    val password: String? = null
) : Parcelable