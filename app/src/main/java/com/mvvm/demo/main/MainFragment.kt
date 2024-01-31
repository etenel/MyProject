package com.mvvm.demo.main

import android.os.Bundle
import com.mvvm.base.BaseFragment
import com.mvvm.demo.BR
import com.mvvm.demo.R
import com.mvvm.demo.databinding.FragmentMainBinding
import com.mvvm.demo.main.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {

    override fun initViewObservable() {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun hideRefresh() {

    }

    override fun hideLoadMore() {

    }

    override fun initView(): Int {
        return R.layout.fragment_main
    }

    override fun initVariableId(): Int {
        return BR.vm
    }
}