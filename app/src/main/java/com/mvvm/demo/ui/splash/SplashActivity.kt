package com.mvvm.demo.ui.splash

import android.os.Bundle
import com.mvvm.base.BaseActivity
import com.mvvm.demo.BR
import com.mvvm.demo.R
import com.mvvm.demo.databinding.ActivitySplashBinding
import com.mvvm.demo.ui.splash.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override fun initView(): Int {
        return R.layout.activity_splash
    }

    override fun initVariableId(): Int {
        return BR.vm
    }

    override fun initViewObservable() {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun hideRefresh() {

    }

    override fun hideLoadMore() {
    }
}