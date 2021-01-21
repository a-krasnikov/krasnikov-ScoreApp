package krasnikov.project.scoreapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentWinnerBinding

class WinnerFragment : Fragment(R.layout.fragment_winner) {
    private lateinit var binding: FragmentWinnerBinding

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

        // back to start screen
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = WinnerFragment()
    }
}