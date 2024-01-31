package com.mvvm.demo.entity

import kotlinx.serialization.Serializable

@Serializable
data class BaseData<T>(
    val code: Int = 0,
    val msg: String = "",
    val `data`: T?=null
)


