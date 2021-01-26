package krasnikov.project.scoreapp.model

import androidx.lifecycle.ViewModel
import kotlin.collections.HashSet

class StandingsViewModel : ViewModel() {

    private val setOfTeams: MutableSet<Team> by lazy { HashSet<Team>() }

    private val sortAsc: (Team) -> Int? = { it.scores }

    val listOfTeamsAsc: List<Team>
        get() = setOfTeams.sortedBy(sortAsc)

    val listOfTeamsDesc: List<Team>
        get() = setOfTeams.sortedByDescending(sortAsc)

    fun addTeam(team: Team) {
        val index = setOfTeams.indexOf(team)
        if (index != -1) {
            setOfTeams.elementAt(index).scores += team.scores
            return
        }
        setOfTeams.add(team)
    }

    fun clear(): MutableList<Team> {
        setOfTeams.clear()
        return setOfTeams.toMutableList()
    }
}