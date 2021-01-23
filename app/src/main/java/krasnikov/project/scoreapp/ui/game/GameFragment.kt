package krasnikov.project.scoreapp.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.App
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentGameBinding
import krasnikov.project.scoreapp.model.GameViewModel
import krasnikov.project.scoreapp.model.Team
import krasnikov.project.scoreapp.model.Timer
import krasnikov.project.scoreapp.ui.dialogs.TimerSetupDialog
import krasnikov.project.scoreapp.ui.winner.WinnerFragment
import krasnikov.project.scoreapp.utils.Navigation.navigateToWinnerFragment
import krasnikov.project.scoreapp.utils.NotificationUtils
import krasnikov.project.scoreapp.utils.TimerStringFormatter

class GameFragment : Fragment(R.layout.fragment_game), TimerSetupDialog.TimerSetupDialogListener {

    private lateinit var binding: FragmentGameBinding

    private val game: GameViewModel by viewModels {
        GameViewModel.GameViewModelFactory(
            Team(requireArguments().getString(ARG_TEAM1, "")),
            Team(requireArguments().getString(ARG_TEAM2, "")),
            Timer(10)
        )
    }

    override fun onResume() {
        super.onResume()
        // Hide notification while GameFragment is active
        NotificationUtils.hideNotification(requireContext(), game)
        setupGameCallback()
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
        setupListeners()
        updateTimerUI(game.timer.secondsRemaining)
    }

    override fun onPause() {
        super.onPause()
        clearGameCallback()
        if (!requireActivity().isChangingConfigurations && game.isRunning) {
            // Show notification while GameFragment is paused
            NotificationUtils.showNotification(requireContext(), game)
        }
    }

    override fun onTimerSetupResult(timeInSecond: Int) {
        updateTimerUI(timeInSecond)
    }

    private fun setupText() {
        with(binding.scoreboard) {
            tvNameTeam1.text = game.team1.name
            tvNameTeam2.text = game.team2.name
        }

        with(binding) {
            btnScoreTeam1.text = getString(R.string.btn_score_team, game.team1)
            btnScoreTeam2.text = getString(R.string.btn_score_team, game.team2)
        }
    }

    private fun setupListeners() {
        binding.btnScoreTeam1.setOnClickListener {
            if (game.isRunning)
                game.incScoreTeam1()
            else
                showMsgGameNotRunning()
        }

        binding.btnScoreTeam2.setOnClickListener {
            if (game.isRunning)
                game.incScoreTeam2()
            else
                showMsgGameNotRunning()
        }

        binding.btnCloseGame.setOnClickListener {
            showCloseGameConfirmationDialog()
        }

        binding.scoreboard.tvTimer.setOnClickListener {
            TimerSetupDialog().apply {
                setTargetFragment(this, 0)
            }.show(parentFragmentManager, "TimerSetupDialog")
        }

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

    private fun setupGameCallback() {
        with(game) {
            timer.timeUpdateCallback = { secondsRemaining ->
                updateTimerUI(secondsRemaining)
            }

            onScoreChangeCallback = { scoreTeam1, scoreTeam2 ->
                binding.scoreboard.tvScoreTeam1.text = scoreTeam1.toString()
                binding.scoreboard.tvScoreTeam2.text = scoreTeam2.toString()
            }

            onGameFinishCallback = {
                val app = requireActivity().application as App
                app.tournamentTable.addTeam(it.first)
                app.tournamentTable.addTeam(it.second)
                val winner = when {
                    it.first.scores > it.second.scores -> it.first.name
                    it.first.scores < it.second.scores -> it.second.name
                    else -> getString(R.string.text_draw)
                }
                navigateToWinnerFragment(parentFragmentManager, winner)
            }
        }
    }

    private fun clearGameCallback() {
        with(game) {
            timer.timeUpdateCallback = null
            onScoreChangeCallback = null
            onGameFinishCallback = null
        }
    }

    private fun updateTimerUI(secondsRemaining: Int) {
        binding.scoreboard.tvTimer.text = TimerStringFormatter.formatTimeRemaining(secondsRemaining)
    }

    private fun showCloseGameConfirmationDialog() {
        //TODO lifecycle
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
        if (game.isRunning) {
            game.stop()
        }
        parentFragmentManager.popBackStack()
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