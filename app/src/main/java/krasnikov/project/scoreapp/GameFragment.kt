package krasnikov.project.scoreapp

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.databinding.FragmentGameBinding
import krasnikov.project.scoreapp.databinding.FragmentStartBinding

class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var binding: FragmentGameBinding

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

        setupButtonListeners()
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
        @JvmStatic
        fun newInstance() = GameFragment()
    }
}