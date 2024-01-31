package com.mvvm.demo.main

import android.os.Bundle
import com.mvvm.base.BaseFragment
import com.mvvm.demo.BR
import com.mvvm.demo.R
import com.mvvm.demo.databinding.FragmentBlankBinding
import com.mvvm.demo.main.viewModel.BlankViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankFragment : BaseFragment<FragmentBlankBinding, BlankViewModel>() {


    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun hideRefresh() {
    }

    override fun hideLoadMore() {
    }

    override fun initViewObservable() {

    }

    override fun initView(): Int {
        return R.layout.fragment_blank
    }

    override fun initVariableId(): Int {
        return BR.vm
    }

}