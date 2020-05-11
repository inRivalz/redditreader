package com.inrivalz.redditreader.util

import java.util.Date
import java.util.concurrent.TimeUnit
import org.ocpsoft.prettytime.PrettyTime

fun Long.toPrettyDate(): String {
    val date = Date(TimeUnit.SECONDS.toMillis(this))
    return PrettyTime().format(date)
}

fun Int.toThousandString(): String {
    return if (this < 1000) {
        this.toString()
    } else {
        val thousand = this / 1000
        val hundred = (this % 1000) / 100
        "$thousand.${hundred}k"
    }
}
