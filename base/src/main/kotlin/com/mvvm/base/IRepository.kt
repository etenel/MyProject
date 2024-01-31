package com.mvvm.base

import androidx.lifecycle.LifecycleEventObserver

interface IRepository : LifecycleEventObserver {
    fun onDestroy()
}