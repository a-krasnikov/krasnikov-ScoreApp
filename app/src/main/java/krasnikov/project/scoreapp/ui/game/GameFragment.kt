package krasnikov.project.scoreapp.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentGameBinding
import krasnikov.project.scoreapp.model.GameViewModel
import krasnikov.project.scoreapp.model.StandingsViewModel
import krasnikov.project.scoreapp.model.Team
import krasnikov.project.scoreapp.model.Timer
import krasnikov.project.scoreapp.ui.dialogs.TimerSetupDialog
import krasnikov.project.scoreapp.utils.Navigation.navigateToWinnerFragment
import krasnikov.project.scoreapp.utils.NotificationUtils
import krasnikov.project.scoreapp.utils.TimerStringFormatter

class GameFragment : Fragment(), TimerSetupDialog.TimerSetupDialogListener {

    private lateinit var binding: FragmentGameBinding

    private val standings: StandingsViewModel by activityViewModels()

    private val game: GameViewModel by viewModels() {
        GameViewModel.GameViewModelFactory(
            Team(requireArguments().getString(ARG_TEAM_ONE, "")),
            Team(requireArguments().getString(ARG_TEAM_TWO, "")),
            Timer()
        )
    }

    override fun onResume() {
        super.onResume()
        updateUI()

        // Hide notification while GameFragment is active
        NotificationUtils.hideNotification(requireContext(), game)

        if (game.isFinished) {
            finishGame()
        }

        // observe the game events
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

        setupBackPressedDispatcher()
        setupTeamsName()
        setupListeners()
    }

    override fun onPause() {
        super.onPause()
        clearGameCallback()
        if (!requireActivity().isChangingConfigurations && game.isRunning) {
            // Show notification while GameFragment is paused
            NotificationUtils.showNotification(requireContext(), game)
        }
    }

    // get result from TimerSetupDialog
    override fun onTimerSetupResult(timeInMillis: Long) {
        updateTimerUI(timeInMillis)
        game.timer.lengthInMillis = timeInMillis
    }

    private fun setupTeamsName() {
        with(binding.scoreboard) {
            tvNameTeam1.text = game.teamOne.name
            tvNameTeam2.text = game.teamTwo.name
        }

        with(binding) {
            btnScoreTeam1.text = getString(R.string.btn_score_team, game.teamOne.name)
            btnScoreTeam2.text = getString(R.string.btn_score_team, game.teamTwo.name)
        }
    }

    private fun setupListeners() {
        binding.btnScoreTeam1.setOnClickListener {
            if (game.isRunning)
                binding.scoreboard.tsScoreTeam1.setText(game.incScoreTeamOne().toString())
            else
                showMsgGameNotRunning()
        }

        binding.btnScoreTeam2.setOnClickListener {
            if (game.isRunning)
                binding.scoreboard.tsScoreTeam2.setText(game.incScoreTeamTwo().toString())
            else
                showMsgGameNotRunning()
        }

        binding.btnCloseGame.setOnClickListener {
            showCloseGameConfirmationDialog()
        }

        binding.scoreboard.tvTimer.setOnClickListener {
            if (!game.isRunning) {
                TimerSetupDialog.newInstance().apply {
                    setTargetFragment(this@GameFragment, 0)
                }.show(parentFragmentManager, "TimerSetupDialog")

            } else {
                showMsgGameRunning()
            }
        }

        binding.fabPlay.setOnClickListener {
            game.start()
        }

        binding.fabPause.setOnClickListener {
            game.pause()
        }

        binding.fabStop.setOnClickListener {
            game.stop()
            updateUI()
        }
    }

    private fun setupBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (game.isRunning) {
                game.stop()
            }
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupGameCallback() {
        with(game) {
            timer.onTickCallback = { millisRemaining ->
                updateTimerUI(millisRemaining)
            }

            onGameFinishCallback = {
                finishGame()
            }
        }
    }

    private fun clearGameCallback() {
        with(game) {
            timer.onTickCallback = null
            onGameFinishCallback = null
        }
    }

    private fun updateUI() {
        updateTimerUI(game.timer.millisRemaining)
        binding.scoreboard.tsScoreTeam1.setCurrentText(game.teamOne.scores.toString())
        binding.scoreboard.tsScoreTeam2.setCurrentText(game.teamTwo.scores.toString())
    }

    private fun updateTimerUI(millisRemaining: Long) {
        binding.scoreboard.tvTimer.text = TimerStringFormatter.formatTime(millisRemaining)
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
        Toast.makeText(
            requireContext(),
            getString(R.string.msg_game_not_running),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showMsgGameRunning() {
        Toast.makeText(
            requireContext(),
            getString(R.string.msg_game_running),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun finishGame() {
        standings.addTeam(game.teamOne)
        standings.addTeam(game.teamTwo)
        navigateToWinnerFragment(parentFragmentManager, game.teamOne, game.teamTwo)
    }

    private fun closeGame() {
        if (game.isRunning) {
            game.stop()
        }
        parentFragmentManager.popBackStack()
    }

    companion object {
        private const val ARG_TEAM_ONE = "teamOne"
        private const val ARG_TEAM_TWO = "teamTwo"

        @JvmStatic
        fun newInstance(team1: String, team2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TEAM_ONE, team1)
                    putString(ARG_TEAM_TWO, team2)
                }
            }
    }
}