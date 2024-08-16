package com.app.legendkebabs.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("date")
fun setDate(view: TextView, date: String?) {
    date?.let {
        view.text = Utils.formatTime(date)
    } ?: view.gone()

}