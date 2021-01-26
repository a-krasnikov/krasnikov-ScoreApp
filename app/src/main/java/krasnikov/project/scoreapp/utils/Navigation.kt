package krasnikov.project.scoreapp.utils

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.model.Team
import krasnikov.project.scoreapp.ui.game.GameFragment
import krasnikov.project.scoreapp.ui.standings.StandingsFragment
import krasnikov.project.scoreapp.ui.winner.WinnerFragment

object Navigation {

    fun navigateToGameFragment(
        fragmentManager: FragmentManager,
        teamName1: String,
        teamName2: String
    ) {
        fragmentManager.commit {
            setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            val fragment = GameFragment.newInstance(
                teamName1,
                teamName2
            )
            replace(R.id.fragment_container, fragment)
            addToBackStack("GameFragment")
            setReorderingAllowed(true)
        }
    }

    fun navigateToWinnerFragment(fragmentManager: FragmentManager, teamOne: Team, teamTwo: Team) {
        val fragment = WinnerFragment.newInstance(teamOne, teamTwo)
        fragmentManager.commit {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
            addToBackStack("WinnerFragment")
        }
    }

    fun navigateToStandingsFragment(fragmentManager: FragmentManager) {
        fragmentManager.commit {
            setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            setReorderingAllowed(true)
            replace<StandingsFragment>(R.id.fragment_container)
            addToBackStack("StandingsFragment")
        }
    }

    fun exitApp(mainActivity: Activity) {
        mainActivity.finish()
    }
}