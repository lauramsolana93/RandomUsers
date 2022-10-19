package com.adevinta.randomusers.common.utils

import android.view.View

// Extensions to control de views visibility

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visibleOrGone(visible: Boolean) {
    this.visibility = View.VISIBLE.takeIf { visible } ?: View.GONE
}