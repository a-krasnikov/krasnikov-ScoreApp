package krasnikov.project.scoreapp.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import krasnikov.project.scoreapp.App
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentGameBinding
import krasnikov.project.scoreapp.databinding.FragmentTeamListBinding

class TeamListFragment : Fragment(R.layout.fragment_team_list) {

    private lateinit var binding: FragmentTeamListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeamListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvGames)

        val app = requireActivity().application as App
        recyclerView.adapter = TeamAdapter(app.tournamentTable.listOfTeams.sortedBy { it.scores })
    }

    companion object {
        @JvmStatic
        fun newInstance() = TeamListFragment()
    }
}