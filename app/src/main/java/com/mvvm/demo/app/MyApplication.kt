package com.mvvm.demo.app

import android.app.Application
import android.content.Context
import com.mvvm.base.utils.LogUtils
import com.mvvm.demo.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlin.properties.Delegates
@HiltAndroidApp
class MyApplication:Application() {
    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
        LogUtils.config.setLogSwitch(BuildConfig.LOG_ENABLE)
            .setConsoleSwitch(BuildConfig.LOG_ENABLE)

    }

}