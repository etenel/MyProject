package com.mvvm.demo.ui.login.di

import com.mvvm.base.IRepository
import com.mvvm.demo.ui.login.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LoginModule {
    @Binds
    fun bindLoginRepository(loginRepository: LoginRepository): IRepository

}