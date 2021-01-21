package krasnikov.project.scoreapp.domain

import android.os.CountDownTimer

class Timer(
    millisInFuture: Long,
    var countDownInterval: Long = 1000L
) {
    private lateinit var countDownTimer: CountDownTimer

    var millisInFuture: Long = millisInFuture
        set(value) {
            field = value
            stop()
        }
    private var millisRemaining = millisInFuture

    var state = TimerState.STOPPED
        private set

    var callback: TimerCallback? = null

    fun start() {
        if (state != TimerState.RUNNING && state != TimerState.FINISHED) {
            countDownTimer = object : CountDownTimer(millisRemaining, countDownInterval) {
                override fun onTick(millis: Long) {
                    millisRemaining = millis
                    callback?.onTick(millis)
                }

                override fun onFinish() {
                    finish()
                }
            }.start()
            state = TimerState.RUNNING
        }
    }

    fun pause() {
        if (state == TimerState.RUNNING) {
            countDownTimer.cancel()
            state = TimerState.PAUSED
        }
    }

    fun stop() {
        if (state != TimerState.STOPPED) {
            countDownTimer.cancel()
            millisRemaining = millisInFuture
            state = TimerState.STOPPED
        }
    }

    private fun finish() {
        state = TimerState.FINISHED
        callback?.onFinish()
    }

    interface TimerCallback {
        fun onTick(millis: Long)

        fun onFinish()
    }

    enum class TimerState {
        STOPPED, PAUSED, RUNNING, FINISHED
    }
}