package com.amrabed.tripplan.view

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.joda.time.LocalDate
import java.util.*

object BindingAdapters {
    @BindingAdapter("android:text")
    @JvmStatic
    fun updateDate(view: TextView, date: Date?) {
        if (date != null) {
            view.text = LocalDate.fromDateFields(date).toString(DATE_PATTERN)
        }
    }

    @BindingAdapter("android:src", "placeholder", "crop", requireAll = false)
    @JvmStatic
    fun loadImage(
        frame: ImageView,
        url: String?,
        placeholder: Int = android.R.color.darker_gray,
        circleCrop: Boolean = false
    ) {
        if (circleCrop) {
            Glide.with(frame).load(url).placeholder(placeholder).circleCrop().into(frame)
        } else {
            Glide.with(frame).load(url).placeholder(placeholder).into(frame)
        }
    }
}

const val DATE_PATTERN = "EEE, MMM d"