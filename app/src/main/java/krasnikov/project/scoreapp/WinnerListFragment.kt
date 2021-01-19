package krasnikov.project.scoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import krasnikov.project.scoreapp.databinding.FragmentStartBinding
import krasnikov.project.scoreapp.databinding.FragmentWinnerListBinding

class WinnerListFragment : Fragment(R.layout.fragment_winner_list) {

    private lateinit var binding: FragmentWinnerListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWinnerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance() = WinnerListFragment()
    }

}