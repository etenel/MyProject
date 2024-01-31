package com.mvvm.demo.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneInfo(
    @SerialName("city")
    val city: String = "",
    @SerialName("province")
    val province: String = "",
    @SerialName("sp")
    val sp: String = ""
)
