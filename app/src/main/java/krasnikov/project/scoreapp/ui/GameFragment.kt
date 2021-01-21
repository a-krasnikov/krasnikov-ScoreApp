package krasnikov.project.scoreapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentGameBinding
import krasnikov.project.scoreapp.domain.Game
import krasnikov.project.scoreapp.domain.Timer

class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var binding: FragmentGameBinding

    private lateinit var team1: String
    private lateinit var team2: String

    private val game by lazy {
        Game().apply {
            timerTickCallback = { millisRemaining ->
                updateTimerUI(millisRemaining / 1000)
            }

            onScoreChangeCallback = { scoreTeam1, scoreTeam2 ->
                binding.scoreboard.tvScoreTeam1.text = scoreTeam1.toString()
                binding.scoreboard.tvScoreTeam2.text = scoreTeam2.toString()
            }

            onGameFinishCallback = {
                navigateToWinnerFragment()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
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

        setupText()
        setupButtonListeners()
        setupGameController()
    }

    private fun setupText() {
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
        binding.btnScoreTeam1.setOnClickListener {
            if (game.isActive)
                game.incScoreTeam1()
            else
                showMsgGameNotRunning()
        }

        binding.btnScoreTeam2.setOnClickListener {
            if (game.isActive)
                game.incScoreTeam2()
            else
                showMsgGameNotRunning()
        }

        binding.btnCloseGame.setOnClickListener {
            showCloseGameConfirmationDialog()
        }
    }

    private fun setupGameController() {
        binding.fabPlay.setOnClickListener {
            game.start()
        }

        binding.fabPause.setOnClickListener {
            game.pause()
        }

        binding.fabStop.setOnClickListener {
            game.stop()
        }
    }

    private fun updateTimerUI(secondsRemaining: Long) {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.scoreboard.tvTimer.text =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"
    }

    private fun showCloseGameConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title_close_game))
            .setMessage(getString(R.string.msg_close_game))
            .setNegativeButton(resources.getString(R.string.action_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.action_yes)) { dialog, _ ->
                dialog.dismiss()
                closeGame()
            }
            .show()
    }

    private fun showMsgGameNotRunning() {
        Toast.makeText(requireContext(), R.string.msg_game_not_running, Toast.LENGTH_SHORT).show()
    }

    private fun closeGame() {
        if (game.isActive) {
            game.stop()
        }
        parentFragmentManager.popBackStack()
    }

    private fun navigateToWinnerFragment() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<WinnerFragment>(R.id.fragment_container)
            addToBackStack("WinnerListFragment")
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