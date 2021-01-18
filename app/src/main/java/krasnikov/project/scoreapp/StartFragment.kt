package krasnikov.project.scoreapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {

    private val binding by lazy { FragmentStartBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.btnStart.setOnClickListener { }

        binding.btnWinners.setOnClickListener { }

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
        fun newInstance(param1: String, param2: String) =
            StartFragment()
    }
}