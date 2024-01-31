package com.mvvm.demo.ui.splash.di

import com.mvvm.base.IRepository
import com.mvvm.demo.ui.splash.repository.SplashRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SplashModule {
    @Binds
    fun bindSplashRepository(repository: SplashRepository): IRepository

}