package com.mvvm.demo.ui.login.repository

import com.mvvm.base.BaseRepository
import com.mvvm.demo.entity.BaseData
import com.mvvm.demo.http.RetrofitClient
import com.mvvm.demo.model.PhoneInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepository @Inject constructor()  : BaseRepository() {

    fun login(name: String): Flow<BaseData<PhoneInfo?>> = flow {
        emit(RetrofitClient.service.login(name))
    }
}
