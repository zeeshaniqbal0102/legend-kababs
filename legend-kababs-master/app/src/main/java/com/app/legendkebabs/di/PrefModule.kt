package com.app.legendkebabs.di

import android.content.Context
import android.content.SharedPreferences
import com.app.legendkebabs.app.LegendKebabs
import com.app.legendkebabs.utils.constants.Constant.SHARED_PREF_FILE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object PrefModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(): SharedPreferences =
        LegendKebabs.getAppContext().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
}