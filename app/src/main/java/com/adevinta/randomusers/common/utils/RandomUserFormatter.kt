package com.adevinta.randomusers.common.utils

import java.text.SimpleDateFormat

fun String.dateFormatter(): String {
    var format = SimpleDateFormat(DATE_FORMAT)
    val date = format.parse(this).apply {
        format = SimpleDateFormat(SIMPLE_DATE_FORMAT)
        format.format(this)
    }
    return date.toString()
}