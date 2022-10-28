package com.bard.gplearning.extensions

import android.content.res.Resources
import android.util.TypedValue

/**
 * float的 px转dp
 */
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
/**
 * int的 px转dp
 */
val Int.dp
    get() = this.toFloat().dp

