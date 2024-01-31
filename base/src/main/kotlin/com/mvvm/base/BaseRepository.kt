package com.mvvm.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

open class BaseRepository:IRepository {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event==Lifecycle.Event.ON_DESTROY){
            source.lifecycle.removeObserver(this)
        }
    }

    override fun onDestroy() {

    }
}