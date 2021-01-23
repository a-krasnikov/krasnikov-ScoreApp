package krasnikov.project.scoreapp.model

import kotlin.collections.HashSet

class TournamentTable {

    private val mutableListOfTeams: MutableSet<Team> by lazy { HashSet<Team>() }

    val listOfTeams: Set<Team>
        get() = mutableListOfTeams

    fun addTeam(team: Team) {
        val index = mutableListOfTeams.indexOf(team)
        if (index != -1) {
            mutableListOfTeams.elementAt(index).scores += team.scores
            return
        }
        mutableListOfTeams.add(team)
    }
}