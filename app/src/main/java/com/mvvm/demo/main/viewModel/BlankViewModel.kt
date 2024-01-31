package com.mvvm.demo.main.viewModel

import com.mvvm.base.BaseViewModel
import com.mvvm.demo.main.repository.BlankRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class BlankViewModel @Inject constructor(repository: BlankRepository) :
    BaseViewModel<BlankRepository>(repository) {

}