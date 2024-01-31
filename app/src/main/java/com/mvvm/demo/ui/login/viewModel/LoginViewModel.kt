package com.mvvm.demo.ui.login.viewModel


import androidx.lifecycle.viewModelScope
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.mvvm.base.BaseViewModel
import com.mvvm.demo.http.ResultState
import com.mvvm.demo.http.asResult
import com.mvvm.demo.model.PhoneInfo
import com.mvvm.demo.ui.login.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(model: LoginRepository) :
    BaseViewModel<LoginRepository>(model) {
    val userName = MutableStateFlow("")
     val password = MutableStateFlow("")
    private var _test = MutableSharedFlow<ResultState<PhoneInfo?>>()
    val test get() = _test

    @SingleClick
    fun login() {
        if (userName.value.isEmpty()) return
        viewModelScope.launch {
            repository.login(userName.value).asResult()
                .collect {
                    _test.emit(it)
                    uiState.send(UIState.ShowToastEvent(it.toString()))
                    if (it is ResultState.Success) {
                        val phoneInfo = it.data!!
                        password.value = "${phoneInfo.province}${phoneInfo.city}${phoneInfo.sp}"
                    }

                }
        }


    }
}
