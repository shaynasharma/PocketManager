package com.example.pocketmanager.data

import java.text.SimpleDateFormat
import java.util.*

class ReportItem(
    val reportType: String,
    val reportAmt: String,
    private val longTime: Long,
    val dayType: String
) {

    val reportTime: String
        get() = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(longTime))

    val reportDayTime: String
        get() = SimpleDateFormat("cccc HH:mm", Locale.ENGLISH).format(Date(longTime))

    val reportDateTime: String
        get() = SimpleDateFormat("d/MMM/yyyy HH:mm", Locale.ENGLISH).format(Date(longTime))

    val reportDate: String
        get() = SimpleDateFormat("d/MMM/yyyy", Locale.ENGLISH).format(Date(longTime))

    val reportWeek: String
        get() = "Week-" + SimpleDateFormat("W", Locale.ENGLISH).format(Date(longTime)) + " of " + SimpleDateFormat(
            "MMM"
        , Locale.ENGLISH).format(Date(longTime))

    val reportMonth: String
        get() = SimpleDateFormat("MMMM", Locale.ENGLISH).format(Date(longTime))

}