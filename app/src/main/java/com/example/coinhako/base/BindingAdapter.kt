package com.example.coinhako.base

import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @author longtran
 * @since 29/05/2021
 */

/**
 * Sets a image url to the imageview by using Glide
 * @param view
 * @param url
 */
@BindingAdapter("android:srcUrl")
fun setSrcUrl(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }
}

/**
 * Sets visibility of a view based on the condition
 * @param view
 * @param isVisible - condition to show/hide a view
 */
@BindingAdapter("android:viewVisible")
fun bindViewVisible(view: View, isVisible: Boolean?) {
    if (isVisible == null) return

    if (isVisible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

/**
 * Sets a TextWatcher to an Edittext
 * @param view
 * @param listener - TextWatcher
 */
@BindingAdapter("android:textChangeListener")
fun bindTextChangeListener(view: EditText, listener: TextWatcher?) {
    if (listener == null) return

    view.addTextChangedListener(listener)
}