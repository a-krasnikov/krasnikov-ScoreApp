package krasnikov.project.scoreapp.model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import kotlin.math.max

class Timer(private val lengthInSeconds: Int) {

    private val handler: Handler = Handler(Looper.getMainLooper())

    var secondsRemaining = lengthInSeconds
        private set

    // Scheduled to update the timers
    private val timeUpdateRunnable: Runnable = TimeUpdateRunnable()

    var state = State.STOPPED
        private set

    val isRunning: Boolean
        get() = state == State.RUNNING

    var timeUpdateCallback: ((secondsRemaining: Int) -> Unit)? = null
    var finishCallback: (() -> Unit)? = null

    fun start() {
        if (state != State.RUNNING && state != State.FINISHED) {
            state = State.RUNNING
            handler.post(timeUpdateRunnable)
        }
    }

    fun pause() {
        if (state == State.RUNNING) {
            state = State.PAUSED
            handler.removeCallbacks(timeUpdateRunnable)
        }
    }

    fun stop() {
        if (state != State.STOPPED) {
            state = State.STOPPED
            handler.removeCallbacks(timeUpdateRunnable)
            secondsRemaining = lengthInSeconds
            timeUpdateCallback?.invoke(secondsRemaining)
        }
    }

    private fun finish() {
        state = State.FINISHED
        finishCallback?.invoke()
    }

    private fun updateTime(): State {
        secondsRemaining--
        timeUpdateCallback?.invoke(secondsRemaining)

        if (secondsRemaining == 0)
            finish()

        return state
    }

    private inner class TimeUpdateRunnable : Runnable {
        override fun run() {
            val startTime = SystemClock.elapsedRealtime()
            // If no timers require continuous updates, avoid scheduling the next update.
            if (updateTime() != State.RUNNING) {
                return
            }
            val endTime = SystemClock.elapsedRealtime()

            // Try to maintain a consistent period of time between redraws.
            val delay = max(0, startTime + 20 - endTime)
            handler.postDelayed(this, 1000)
        }
    }

    enum class State {
        STOPPED, PAUSED, RUNNING, FINISHED
    }
}