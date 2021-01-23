package krasnikov.project.scoreapp.utils

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.ui.game.GameFragment
import krasnikov.project.scoreapp.ui.teams.TeamListFragment
import krasnikov.project.scoreapp.ui.winner.WinnerFragment

object Navigation {

    fun navigateToGameFragment(
        fragmentManager: FragmentManager,
        teamName1: String,
        teamName2: String
    ) {
        fragmentManager.commit {
            val fragment = GameFragment.newInstance(
                teamName1,
                teamName2
            )
            replace(R.id.fragment_container, fragment)
            addToBackStack("GameFragment")
            setReorderingAllowed(true)
        }
    }

    fun navigateToWinnerFragment(fragmentManager: FragmentManager, winner: String) {
        val fragment = WinnerFragment.newInstance(winner)
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
            addToBackStack("WinnerFragment")
        }
    }

    fun navigateToTeamListFragment(fragmentManager: FragmentManager) {
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace<TeamListFragment>(R.id.fragment_container)
            addToBackStack("TeamListFragment")
        }
    }

    fun exitApp(mainActivity: Activity) {
        mainActivity.finish()
    }
}