package com.mvvm.demo.main.di

import com.mvvm.base.IRepository
import com.mvvm.demo.main.repository.BlankRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MainModule {
    @Binds
    fun bindsBlankRepository(repository: BlankRepository):IRepository
}