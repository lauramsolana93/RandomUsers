package com.adevinta.randomusers.common.utils

import java.text.SimpleDateFormat

fun String.dateFormater(): String {
    var format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    val newDate = format.parse(this)
    format = SimpleDateFormat("dd/M/yyyy")
    val date = format.format(newDate)
    return date.toString()
}