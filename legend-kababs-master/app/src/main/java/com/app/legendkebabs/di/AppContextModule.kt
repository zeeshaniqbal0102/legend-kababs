package com.app.legendkebabs.di


import android.content.Context
import com.app.legendkebabs.app.LegendKebabs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppContextModule {

    @Singleton
    @Provides
    fun getApplicationContext(): Context = LegendKebabs.getAppContext().applicationContext

}