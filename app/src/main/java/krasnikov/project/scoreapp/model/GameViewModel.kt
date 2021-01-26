package krasnikov.project.scoreapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModel(val teamOne: Team, val teamTwo: Team, val timer: Timer) : ViewModel() {

    val isRunning
        get() = timer.isRunning

    val isFinished
        get() = timer.isFinished

    var onGameFinishCallback: (() -> Unit)? = null

    init {
        // Setup callback finish game
        timer.onFinishCallback = {
            onGameFinishCallback?.invoke()
        }
    }

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

    fun incScoreTeamOne(): Int = ++teamOne.scores

    fun incScoreTeamTwo(): Int = ++teamTwo.scores

    fun resetScore() {
        teamOne.scores = 0
        teamTwo.scores = 0
    }

    class GameViewModelFactory(
        private val teamOne: Team,
        private val teamTwo: Team,
        private val timer: Timer
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                return GameViewModel(teamOne, teamTwo, timer) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}