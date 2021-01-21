package krasnikov.project.scoreapp.domain

class Game(timeInMillis: Long = 10000) {

    var timeInMillis: Long = timeInMillis
        set(value) {
            timer.millisInFuture = value
            field = value
        }

    private val timer by lazy {
        Timer(timeInMillis).apply {
            callback = object : Timer.TimerCallback {
                override fun onTick(millis: Long) {
                    timerTickCallback?.invoke(millis)
                }

                override fun onFinish() {
                    onGameFinishCallback?.invoke()
                }
            }
        }
    }

    var timerTickCallback: ((millisRemaining: Long) -> Unit)? = null

    private var scoreTeam1 = 0
    private var scoreTeam2 = 0

    val isActive
        get() = timer.state == Timer.TimerState.RUNNING

    var onScoreChangeCallback: ((scoreTeam1: Int, scoreTeam2: Int) -> Unit)? = null
    var onGameFinishCallback: (() -> Unit)? = null

    fun start() {
        timer.start()
    }

    fun pause() {
        timer.pause()
    }

    fun stop() {
        timer.stop()
        resetScore()
    }

    fun incScoreTeam1() {
        scoreTeam1++
        scoreChanged()
    }

    fun incScoreTeam2() {
        scoreTeam2++
        scoreChanged()
    }

    private fun scoreChanged() {
        onScoreChangeCallback?.invoke(scoreTeam1, scoreTeam2)
    }

    fun resetScore() {
        scoreTeam1 = 0
        scoreTeam2 = 0
        scoreChanged()
    }
}