package krasnikov.project.scoreapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModel(val team1: Team, val team2: Team, val timer: Timer) : ViewModel() {

    val isRunning
        get() = timer.isRunning

    var onScoreChangeCallback: ((scoreTeam1: Int, scoreTeam2: Int) -> Unit)? = null
    var onGameFinishCallback: ((teams: Pair<Team, Team>) -> Unit)? = null

    init {
        timer.finishCallback = {
            onGameFinishCallback?.invoke(team1 to team2)
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

    fun incScoreTeam1() {
        team1.scores++
        scoreChanged()
    }

    fun incScoreTeam2() {
        team2.scores++
        scoreChanged()
    }

    fun resetScore() {
        team1.scores = 0
        team2.scores = 0
        scoreChanged()
    }

    private fun scoreChanged() {
        onScoreChangeCallback?.invoke(team1.scores, team2.scores)
    }

    class GameViewModelFactory(
        private val team1: Team,
        private val team2: Team,
        private val timer: Timer
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                return GameViewModel(team1, team2, timer) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}