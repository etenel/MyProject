package com.mvvm.demo.main.viewModel

import com.mvvm.base.BaseViewModel
import com.mvvm.demo.main.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(model: MainRepository) :
    BaseViewModel<MainRepository>(model) {

}