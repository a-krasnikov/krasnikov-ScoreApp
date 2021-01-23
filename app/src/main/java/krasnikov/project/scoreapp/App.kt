package krasnikov.project.scoreapp

import android.app.Application
import krasnikov.project.scoreapp.model.Team
import krasnikov.project.scoreapp.model.TournamentTable

class App : Application() {

    val tournamentTable: TournamentTable by lazy { TournamentTable() }

    override fun onCreate() {
        super.onCreate()
    }


}