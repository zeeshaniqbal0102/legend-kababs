package com.app.legendkebabs.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LegendKebabs : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        FragmentManager.enableDebugLogging(true) //todo debug
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this
    }

    companion object {
        val TAG: String = LegendKebabs::class.java
            .simpleName
        lateinit var ctx: LegendKebabs

        fun getAppContext(): LegendKebabs {
            return ctx
        }
    }
}