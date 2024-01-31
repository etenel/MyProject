package com.mvvm.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.InflateException
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>>() : AppCompatActivity(),
    IActivity {
    protected lateinit var binding: V
    protected lateinit var viewModel: VM
    private var viewModelId: Int = 0
    val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            isEnabled = false
            onBackPressedDispatcher.onBackPressed()
            isEnabled = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //8.0透明activity方向问题
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            fixOrientation()
        }
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        try {
            val layoutResID: Int = initView()
            if (layoutResID != 0) {
                binding = DataBindingUtil.setContentView(this, layoutResID)
            }
        } catch (e: Exception) {
            if (e is InflateException) throw e
            e.printStackTrace()
        }
        viewModelId = initVariableId()
        //私有的初始化ViewModel方法
        val modelClass: Class<VM> =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
//        viewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        ).get(modelClass)
        viewModel = ViewModelProvider(this).get(modelClass)
        viewModel.injectLifecycle(lifecycle)
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        //页面数据初始化方法
        initData(savedInstanceState)
        binding.lifecycleOwner = this
        if (viewModelId != 0) {
            binding.setVariable(viewModelId, viewModel)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    @SuppressLint("PrivateApi")
    private fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable").getField("Window")[null] as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m: Method = ActivityInfo::class.java.getMethod(
                "isTranslucentOrFloating", TypedArray::class.java
            )
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun fixOrientation(): Boolean {
        try {
            val field: Field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o = field.get(this) as ActivityInfo
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
    private fun registerUIChangeLiveDataCallBack() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStates.collect {
                    when (it) {
                        BaseViewModel.UIState.DismissDialogEvent -> {
                            TODO()
                        }

                        BaseViewModel.UIState.FinishEvent -> {
                            finish()
                        }

                        BaseViewModel.UIState.FinishLoadMoreEvent -> {
                            hideLoadMore()
                        }

                        BaseViewModel.UIState.FinishRefreshEvent -> {
                            hideRefresh()
                        }

                        is BaseViewModel.UIState.FinishResultEvent -> {
                            setResult(it.resultCode, it.intent)
                        }


                        BaseViewModel.UIState.OnBackPressedEvent -> {
                            backPressedCallback.isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                            backPressedCallback.isEnabled = true
                        }

                        is BaseViewModel.UIState.ShowDialogEvent<*> -> {
                            TODO()
                        }

                        is BaseViewModel.UIState.ShowToastEvent -> {
                            Toast.makeText(baseContext, it.msg, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }

}