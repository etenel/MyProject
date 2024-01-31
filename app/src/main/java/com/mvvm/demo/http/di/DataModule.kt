package com.mvvm.demo.http.di

import com.mvvm.demo.http.NetWorkUtils
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsNetWorkUtils(netWorkUtils: NetWorkUtils):NetWorkUtils
}