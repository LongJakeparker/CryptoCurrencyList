package com.example.coinhako.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout


/**
 * A custom behavior of AppBarLayout to enable/disable the scrolling
 * @author longtran
 * @since 30/05/2021
 */
class DisableAppBarLayoutBehavior : AppBarLayout.Behavior {

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var mEnabled = false

    fun setEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return mEnabled && super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }
}