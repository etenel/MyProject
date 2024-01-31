package com.mvvm.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mvvm.base.utils.LogUtils
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
//使用viewLifecycleOwner代替fragment的lifecycleOwner
@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<*>>() :
    Fragment(), IFragment {
    protected lateinit var binding: V
    protected lateinit var viewModel: VM
    private var viewModelId: Int = 0
    lateinit var mContext: Context
    val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutRes = initView()
        if (layoutRes != 0) {
            binding = DataBindingUtil.inflate(
                inflater,
                initView(),
                container,
                false
            )
            return binding.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(backPressedCallback)
        val modelClass: Class<VM> =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
        viewModel = ViewModelProvider(this).get(modelClass)
        viewModel.injectLifecycle(viewLifecycleOwner.lifecycle)
        registerUIChangeLiveDataCallBack()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        //页面数据初始化方法
        initData(savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModelId = initVariableId()
        if (viewModelId != 0) {
            binding.setVariable(viewModelId, viewModel)
        }
    }

    private fun registerUIChangeLiveDataCallBack() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

            }
            viewModel.uiStates.collect {
                when (it) {
                    BaseViewModel.UIState.DismissDialogEvent -> {

                    }

                    BaseViewModel.UIState.FinishEvent -> {
                        activity?.finish()
                    }

                    BaseViewModel.UIState.FinishLoadMoreEvent -> {
                        hideLoadMore()
                    }

                    BaseViewModel.UIState.FinishRefreshEvent -> {
                        hideRefresh()
                    }

                    is BaseViewModel.UIState.FinishResultEvent -> {
                        activity?.setResult(it.resultCode, it.intent)
                    }


                    BaseViewModel.UIState.OnBackPressedEvent -> {
                        backPressedCallback.isEnabled = false
                        activity?.onBackPressedDispatcher?.onBackPressed()
                        backPressedCallback.isEnabled = true
                    }

                    is BaseViewModel.UIState.ShowDialogEvent<*> -> {

                    }

                    is BaseViewModel.UIState.ShowToastEvent -> {
                        LogUtils.e(it.msg)
                        Toast.makeText(activity, it.msg, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        binding.unbind()
    }
}