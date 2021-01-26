package krasnikov.project.scoreapp.ui.winner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentWinnerBinding
import krasnikov.project.scoreapp.model.Team
import krasnikov.project.scoreapp.utils.Navigation

class WinnerFragment : Fragment() {
    private lateinit var binding: FragmentWinnerBinding

    private val teamOne: Team
            by lazy { requireNotNull(requireArguments().getParcelable(ARG_TEAM_ONE)) }
    private val teamTwo: Team
            by lazy { requireNotNull(requireArguments().getParcelable(ARG_TEAM_TWO)) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWinnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonListeners()
        setupBackPressedDispatcher()
        setupWinner()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(ARG_TEAM_ONE, teamOne)
        outState.putParcelable(ARG_TEAM_TWO, teamTwo)
    }

    private fun setupButtonListeners() {
        binding.btnNewGame.setOnClickListener {
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.btnStandings.setOnClickListener {
            Navigation.navigateToStandingsFragment(parentFragmentManager)
        }

        binding.btnShare.setOnClickListener {
            shareWinner()
        }
    }

    private fun setupBackPressedDispatcher() {
        // back to start screen
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun setupWinner() {
        val winner = when {
            teamOne.scores > teamTwo.scores -> teamOne.name
            teamOne.scores < teamTwo.scores -> teamTwo.name
            else -> getString(R.string.text_draw)
        }

        binding.tvWinner.text = winner
        binding.tvResult.text = getString(R.string.text_result, teamOne.scores, teamTwo.scores)
    }

    private fun shareWinner() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getString(
                    R.string.text_share_result,
                    teamOne.name,
                    teamOne.scores,
                    teamTwo.scores,
                    teamTwo.name
                )
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {
        private const val ARG_TEAM_ONE = "teamOne"
        private const val ARG_TEAM_TWO = "teamTwo"

        @JvmStatic
        fun newInstance(teamOne: Team, teamTwo: Team) = WinnerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TEAM_ONE, teamOne)
                putParcelable(ARG_TEAM_TWO, teamTwo)
            }
        }
    }
}