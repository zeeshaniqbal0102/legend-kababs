package com.app.legendkebabs.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import com.app.legendkebabs.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Utils {
    companion object {
        // SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
        val DATE_TIME_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss"


        fun formatTime(time: String): String {
            val dateParse = SimpleDateFormat(DATE_TIME_FORMAT2)
            val dateDisplay = SimpleDateFormat(DATE_TIME_FORMAT)
            return dateDisplay.format(dateParse.parse(time))
        }
    }
}