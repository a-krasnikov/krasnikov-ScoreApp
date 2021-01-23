package krasnikov.project.scoreapp.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentStartBinding
import krasnikov.project.scoreapp.utils.Navigation

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
            if (isValidInputName(binding.etTeam1) && isValidInputName(binding.etTeam2)) {
                Navigation.navigateToGameFragment(
                    parentFragmentManager,
                    binding.etTeam1.text.toString(),
                    binding.etTeam2.text.toString()
                )
            }
        }

        binding.btnWinners.setOnClickListener {
            Navigation.navigateToTeamListFragment(parentFragmentManager)
        }

        binding.btnExit.setOnClickListener {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title_exit))
            .setMessage(getString(R.string.msg_exit))
            .setNegativeButton(resources.getString(R.string.action_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.action_yes)) { dialog, _ ->
                dialog.dismiss()
                Navigation.exitApp(requireActivity())
            }
            .show()
    }

    private fun setupTextInputValidation() {
        val etLoseFocusListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                isValidInputName(view as TextInputEditText)
            }
        }

        binding.etTeam1.onFocusChangeListener = etLoseFocusListener
        binding.etTeam2.onFocusChangeListener = etLoseFocusListener
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