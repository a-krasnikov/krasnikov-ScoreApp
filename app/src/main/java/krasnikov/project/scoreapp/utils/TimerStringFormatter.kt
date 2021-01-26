package krasnikov.project.scoreapp.utils

import android.text.format.DateUtils

object TimerStringFormatter {
    const val HOUR_IN_SECONDS = 3600
    const val MINUTE_IN_SECONDS = 60
    const val TIME_TEMPLATE = "%02d:%02d:%02d"

    fun formatTime(timeInMillis: Long): String? {
        var roundedHours = (timeInMillis / DateUtils.HOUR_IN_MILLIS).toInt()
        var roundedMinutes = (timeInMillis / DateUtils.MINUTE_IN_MILLIS % 60).toInt()
        var roundedSeconds = (timeInMillis / DateUtils.SECOND_IN_MILLIS % 60).toInt()

        val seconds: Int
        val minutes: Int
        val hours: Int
        if (timeInMillis % DateUtils.SECOND_IN_MILLIS != 0L) {
            // Add 1 because there's a partial second.
            roundedSeconds += 1
            if (roundedSeconds == 60) {
                // Wind back and fix the hours and minutes as needed.
                seconds = 0
                roundedMinutes += 1
                if (roundedMinutes == 60) {
                    minutes = 0
                    roundedHours += 1
                    hours = roundedHours
                } else {
                    minutes = roundedMinutes
                    hours = roundedHours
                }
            } else {
                seconds = roundedSeconds
                minutes = roundedMinutes
                hours = roundedHours
            }
        } else {
            // Already perfect precision, or we don't want to consider seconds at all.
            seconds = roundedSeconds
            minutes = roundedMinutes
            hours = roundedHours
        }

        return String.format(TIME_TEMPLATE, hours, minutes, seconds)
    }
}