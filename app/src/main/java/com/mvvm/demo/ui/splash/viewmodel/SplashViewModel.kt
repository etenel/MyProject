package com.mvvm.demo.ui.splash.viewModel

import com.mvvm.base.BaseViewModel
import com.mvvm.demo.ui.splash.repository.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(model: SplashRepository) :
    BaseViewModel<SplashRepository>(model) {

}