package krasnikov.project.scoreapp.ui.standings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.model.Team

class TeamAdapter : ListAdapter<Team, TeamAdapter.TeamViewHolder>(TeamDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(team: Team) {
            itemView.findViewById<TextView>(R.id.tvTeam).text =
                itemView.resources.getString(R.string.text_item_team, team.name, team.scores)
        }
    }
}

private class TeamDiffCallback : DiffUtil.ItemCallback<Team>() {

    override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem.scores == newItem.scores
    }
}