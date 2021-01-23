package krasnikov.project.scoreapp.ui.teams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.model.Team

class TeamAdapter(private val items: Collection<Team>) :
    RecyclerView.Adapter<TeamAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(items.elementAt(position))
    }

    override fun getItemCount(): Int = items.size

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(team: Team) {
            itemView.findViewById<TextView>(R.id.tvGame).text = "${team.name} - ${team.scores}"
        }
    }
}