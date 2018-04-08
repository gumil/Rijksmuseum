package io.gumil.rijksmuseum.common

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

internal fun String.toClickableSpan(onClick: (String) -> Unit): Spannable {
    return SpannableString(this).apply {
        setSpan(object : ClickableSpan() {
            override fun onClick(widget: View?) {
                onClick(this@toClickableSpan)
            }
        }, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

internal fun TextView.setTextAndVisibility(text: CharSequence?) {
    text?.let {
        this.text = it
        visibility = View.VISIBLE
    } ?: let { visibility = View.GONE }
}