package krasnikov.project.scoreapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentGameBinding

class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var binding: FragmentGameBinding

    private lateinit var team1: String
    private lateinit var team2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            //TODO default
            team1 = it.getString(ARG_TEAM1, "")
            team2 = it.getString(ARG_TEAM2, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTeamName()
        setupButtonListeners()
    }

    private fun setupTeamName() {
        with(binding.scoreboard) {
            tvNameTeam1.text = team1
            tvNameTeam2.text = team2
        }

        with(binding) {
            btnScoreTeam1.text = getString(R.string.btn_score_team, team1)
            btnScoreTeam2.text = getString(R.string.btn_score_team, team2)
        }
    }

    private fun setupButtonListeners() {
        binding.btnScoreTeam1.setOnClickListener { }

        binding.btnScoreTeam2.setOnClickListener { }

        binding.btnCloseGame.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_title_close_game))
                .setMessage(getString(R.string.msg_close_game))
                .setNegativeButton(resources.getString(R.string.action_cancel)) { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.action_yes)) { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    companion object {
        private const val ARG_TEAM1 = "team1"
        private const val ARG_TEAM2 = "team2"

        @JvmStatic
        fun newInstance(team1: String, team2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TEAM1, team1)
                    putString(ARG_TEAM2, team2)
                }
            }
    }
}