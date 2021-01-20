package krasnikov.project.scoreapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import krasnikov.project.scoreapp.R
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
        setupTextInputValidation()
    }

    private fun setupButtonListeners() {
        binding.btnStart.setOnClickListener {
            navigateToGameFragment()
        }

        binding.btnWinners.setOnClickListener {
            navigateToWinnerListFragment()
        }

        binding.btnExit.setOnClickListener {
            showExitConfirmationDialog()
        }
    }

    private fun navigateToGameFragment() {
        if (isValidInputName(binding.etTeam1) && isValidInputName(binding.etTeam2)) {
            parentFragmentManager.commit {
                val fragment = GameFragment.newInstance(
                    binding.etTeam1.text.toString(),
                    binding.etTeam1.text.toString()
                )
                replace(R.id.fragment_container, fragment)
                addToBackStack("GameFragment")
                setReorderingAllowed(true)
            }
        }
    }

    private fun navigateToWinnerListFragment() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<WinnerListFragment>(R.id.fragment_container)
            addToBackStack("WinnerListFragment")
        }
    }

    private fun showExitConfirmationDialog() {
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

    private fun setupTextInputValidation() {
        val etFocusListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                isValidInputName(view as TextInputEditText)
            }
        }

        binding.etTeam1.onFocusChangeListener = etFocusListener
        binding.etTeam2.onFocusChangeListener = etFocusListener
    }

    private fun isValidInputName(inputText: TextInputEditText): Boolean {
        val minLength = 3

        if (inputText.text?.length ?: 0 < minLength) {
            inputText.error = getString(R.string.error_input_team_name)
            return false
        }

        return true
    }

    companion object {
        @JvmStatic
        fun newInstance() = StartFragment()
    }
}