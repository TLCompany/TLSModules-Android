package com.tlsolution.tlsmodules.Extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.formattedString(): String? {
    var calendar = Calendar.getInstance()
    calendar.time = Date()
    val cYear = calendar.get(Calendar.YEAR)
    val cMonth = calendar.get(Calendar.MONTH)
    val cDay = calendar.get(Calendar.DAY_OF_MONTH)
    val cHour = calendar.get(Calendar.HOUR_OF_DAY)
    val cMin = calendar.get(Calendar.MINUTE)
    val cSec = calendar.get(Calendar.SECOND)

    calendar.time = this
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val min = calendar.get(Calendar.MINUTE)
    val sec = calendar.get(Calendar.SECOND)

    if (cYear == year && cMonth == month && cDay == day) {
        if (cSec == sec && cHour == hour && cMin == min) {
            return "지금"
        } else if (cMin == min && cSec > sec) {
            return "${cSec - sec}초 전"
        } else if (cHour == hour && cMin > min) {
            return "${cMin - min}분 전"
        } else {
            val formatter = SimpleDateFormat("hh:mm a")
            formatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            return formatter.format(this)
        }
    } else if (cMonth == month && (cDay - day) <= 3) {
        val formatter = SimpleDateFormat("yyy.MM.dd")
        formatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        return "${formatter.format(this)} (${cDay - day}일전)"
    } else {
        val formatter = SimpleDateFormat("yyy.MM.dd")
        formatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        return formatter.format(this)
    }
}