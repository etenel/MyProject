package com.mvvm.demo.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mvvm.base.BaseActivity
import com.mvvm.base.utils.LogUtils
import com.mvvm.demo.BR
import com.mvvm.demo.R
import com.mvvm.demo.databinding.ActivityLoginBinding
import com.mvvm.demo.http.ResultState
import com.mvvm.demo.ui.login.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    private var progressBar: ProgressDialog? = null
    override fun initView(): Int {
        return R.layout.activity_login
    }

    override fun initVariableId(): Int {
        return BR.vm
    }


    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun hideRefresh() {
        LogUtils.e("hideLoading")
    }

    override fun hideLoadMore() {
    }

    override fun initViewObservable() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.test.collect {
                    if (it != ResultState.Loading) {
                        hideLoading()
                    }
                    when (it) {
                        is ResultState.Empty -> {
                            LogUtils.e("empty", it.msg)
                        }

                        is ResultState.Error -> {
                            LogUtils.e("error", it.exception?.message.orEmpty())
                        }

                        ResultState.Loading -> {
                            showLoading()
                        }

                        is ResultState.Success -> {
                            LogUtils.e("success")
                        }
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        if (progressBar != null && progressBar?.isShowing == true) {
            progressBar?.dismiss()
        }
    }

    private fun showLoading() {
        if (progressBar == null) {
            progressBar = ProgressDialog(this)
        }
        if (progressBar?.isShowing == false) {
            progressBar?.show()
        }
    }

}