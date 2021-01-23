package krasnikov.project.scoreapp.utils

object TimerStringFormatter {
    const val HOUR_IN_SECONDS = 3600
    const val MINUTE_IN_SECONDS = 60

    fun formatTimeRemaining(remainingTime: Int): String? {
        val hours: Int = remainingTime / HOUR_IN_SECONDS
        val minutes: Int = remainingTime / MINUTE_IN_SECONDS % 60
        val seconds: Int = remainingTime % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}