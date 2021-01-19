package krasnikov.project.scoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonListeners()
    }

    private fun setupButtonListeners() {

        binding.btnStart.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<GameFragment>(R.id.fragment_container)
                addToBackStack("GameFragment")
            }
        }

        binding.btnWinners.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<WinnerListFragment>(R.id.fragment_container)
                addToBackStack("WinnerListFragment")
            }
        }

        binding.btnExit.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_title_exit))
                .setMessage(getString(R.string.msg_exit))
                .setNegativeButton(resources.getString(R.string.action_cancel)) { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.action_yes)) { dialog, which ->
                    dialog.dismiss()
                    requireActivity().finish()
                }
                .show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = StartFragment()
    }
}