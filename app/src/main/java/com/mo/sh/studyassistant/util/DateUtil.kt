package com.mo.sh.studyassistant.util

import android.text.format.DateUtils
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.app.App
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private fun Long.isYesterday(): Boolean {
    val calendar = Calendar.getInstance()
    val currentDay = calendar.get(Calendar.DAY_OF_YEAR)

    calendar.timeInMillis = this
    val targetDay = calendar.get(Calendar.DAY_OF_YEAR)

    return currentDay - targetDay == 1
}

private fun Long.isSameWeek(): Boolean {
    val calendar = Calendar.getInstance()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)

    calendar.timeInMillis = this
    val targetWeek = calendar.get(Calendar.WEEK_OF_YEAR)

    return currentWeek == targetWeek
}

fun Long.isSameMonth(now: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = now
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.timeInMillis = this
    val targetMonth = calendar.get(Calendar.MONTH)
    val targetYear = calendar.get(Calendar.YEAR)

    return currentMonth == targetMonth && currentYear == targetYear
}

fun Long.isSameDay(now: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = now
    val currentDay = calendar.get(Calendar.DAY_OF_YEAR)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.timeInMillis = this
    val targetDay = calendar.get(Calendar.DAY_OF_YEAR)
    val targetYear = calendar.get(Calendar.YEAR)

    return currentDay == targetDay && currentYear == targetYear
}

private fun Long.isSameYear(): Boolean {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.timeInMillis = this
    val targetYear = calendar.get(Calendar.YEAR)

    return currentYear == targetYear
}

fun Long.formatTime(): String {
    val format = when {
        DateUtils.isToday(this) || isYesterday() -> "hh:mm a"
        isSameWeek() -> "EEEE hh:mm a"
        isSameYear() -> "MMM dd hh:mm a"
        else -> "MMM dd, yyyy hh:mm a"
    }
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return (if (isYesterday()) "${App.getString(R.string.yesterday)} " else "") + sdf.format(this)
}