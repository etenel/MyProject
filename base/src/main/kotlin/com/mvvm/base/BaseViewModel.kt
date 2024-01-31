package com.mvvm.base

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


abstract class BaseViewModel<M : IRepository>(val repository: M) : ViewModel(), IViewModel {
    //StateFlow会有一个初始值，旋转屏幕后会重复订阅一次数据。
    //   SharedFlow订阅者共享通知，可以实现一对多的广播
//    存在一个问题，接收器无法接收到 collect 之前发送的事件
//    val uiState = MutableSharedFlow<UIState>()
//    val uiStates get() = uiState
    //Channel每个消息只有一个订阅者可以收到，用于一对一的通信
//    第一个订阅者可以收到 collect 之前的事件
    val uiState = Channel<UIState>()
    val uiStates get() = uiState.receiveAsFlow()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }

    //将 LifecycleObserver 注册给 LifecycleOwner
    fun injectLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        lifecycle.addObserver(repository as LifecycleObserver)
    }


    sealed interface UIState {
        data class ShowDialogEvent<T>(val data: T) : UIState
        data object DismissDialogEvent : UIState
        data object FinishEvent : UIState
        data class FinishResultEvent(val resultCode: Int, val intent: Intent) : UIState
        data object OnBackPressedEvent : UIState
        data class ShowToastEvent(val msg: String = "") : UIState
        data object FinishRefreshEvent : UIState
        data object FinishLoadMoreEvent : UIState
    }


}


