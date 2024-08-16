package com.app.legendkebabs.utils

import android.content.Context
import android.provider.Settings
import android.view.View

/**
 * Extension method to provide show keyboard for View.
 */
fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun getDeviceId(mInstance: Context): String {
    val android_id: String = Settings.Secure.getString(mInstance.contentResolver, Settings.Secure.ANDROID_ID)
    return android_id
}