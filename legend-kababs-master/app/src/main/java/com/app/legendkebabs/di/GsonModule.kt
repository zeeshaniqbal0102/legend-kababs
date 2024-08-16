package com.app.legendkebabs.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object GsonModule {

    @Provides
    @Singleton
    fun provideGson() = Gson()

}