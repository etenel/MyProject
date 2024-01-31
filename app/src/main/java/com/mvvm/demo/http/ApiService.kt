package com.mvvm.demo.http

import com.mvvm.demo.entity.BaseData
import com.mvvm.demo.model.PhoneInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("api/lock/houseinfo")
    suspend fun getLockDatas(@Field("user_id") userId: String, @Field("role_type") int: Int): Any


    @GET("api/phone/guishu-api.php")
    suspend fun login(@Query("phone") phone: String): BaseData<PhoneInfo?>

}