package krasnikov.project.scoreapp.model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.format.DateUtils
import kotlin.properties.Delegates

class Timer(lengthInMillis: Long = 30000L) {

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    // Scheduled to update the timers
    private val timeUpdateRunnable: Runnable = TimeUpdateRunnable()

    var lengthInMillis: Long by Delegates.observable(lengthInMillis) { _, _, newValue ->
        millisRemaining = newValue
    }

    private var nextTickTime = 0L

    var millisRemaining = lengthInMillis
        private set

    var state = State.STOPPED
        private set

    val isRunning: Boolean
        get() = state == State.RUNNING

    val isFinished: Boolean
        get() = state == State.FINISHED

    var onTickCallback: ((millisRemaining: Long) -> Unit)? = null
    var onFinishCallback: (() -> Unit)? = null

    fun start() {
        if (state != State.RUNNING && state != State.FINISHED) {
            state = State.RUNNING
            nextTickTime = SystemClock.uptimeMillis() + DateUtils.SECOND_IN_MILLIS
            handler.postAtTime(timeUpdateRunnable, nextTickTime)
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
            millisRemaining = lengthInMillis
            onTickCallback?.invoke(millisRemaining)
        }
    }

    private fun finish() {
        state = State.FINISHED
        handler.removeCallbacks(timeUpdateRunnable)
        onFinishCallback?.invoke()
    }

    private fun updateTime(): State {
        millisRemaining -= DateUtils.SECOND_IN_MILLIS
        onTickCallback?.invoke(millisRemaining)

        if (millisRemaining <= 0) {
            finish()
        }
        return state
    }

    private inner class TimeUpdateRunnable : Runnable {
        override fun run() {
            if (updateTime() == State.RUNNING) {
                nextTickTime += DateUtils.SECOND_IN_MILLIS
                handler.postAtTime(this, nextTickTime)
            }
        }
    }

    enum class State {
        STOPPED, PAUSED, RUNNING, FINISHED
    }
}