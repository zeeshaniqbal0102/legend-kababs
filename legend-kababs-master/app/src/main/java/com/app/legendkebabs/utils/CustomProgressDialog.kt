package com.app.legendkebabs.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.app.legendkebabs.R
import javax.inject.Inject

class CustomProgressDialog @Inject constructor() {

    lateinit var dialog: CustomDialog

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun isDialogShowing(): Boolean {
        if (this::dialog.isInitialized) {
            return dialog.isShowing
        }
        return false
    }

    fun show(context: Context, title: CharSequence?): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog_view, null)
        val ttle = view.findViewById<TextView>(R.id.cp_title)
        val cardView = view.findViewById<CardView>(R.id.cp_cardview)
        val pBar = view.findViewById<ProgressBar>(R.id.cp_pbar)
        if (title != null) {
            ttle.text = title
//            view..cp_title.text = title
        }

        // Card Color
        cardView.setCardBackgroundColor(Color.parseColor("#70000000"))
//        view.cp_cardview.setCardBackgroundColor(Color.parseColor("#70000000"))

        // Progress Bar Color
        setColorFilter(
            pBar.indeterminateDrawable,
            ResourcesCompat.getColor(context.resources, R.color.colorPrimary, null)
//            view.cp_pbar.indeterminateDrawable,
//            ResourcesCompat.getColor(context.resources, R.color.colorPrimary, null)
        )

        // Text Color
        ttle.setTextColor(Color.WHITE)
//        view.cp_title.setTextColor(Color.WHITE)

        dialog = CustomDialog(context)
        dialog.setContentView(view)
        dialog.show()
        return dialog
    }

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }


    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(android.R.color.transparent)
//            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
//                insets.consumeSystemWindowInsets()
//            }
        }
    }
}