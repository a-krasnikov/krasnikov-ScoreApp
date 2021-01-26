package krasnikov.project.scoreapp.ui.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.FragmentStandingsBinding
import krasnikov.project.scoreapp.model.StandingsViewModel

class StandingsFragment : Fragment() {

    private lateinit var binding: FragmentStandingsBinding
    private val standings: StandingsViewModel by activityViewModels()
    private lateinit var adapter: TeamAdapter
    private var sortedAsc = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            sortedAsc = it.getBoolean(BUNDLE_SORTED_ASC, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupRecyclerView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BUNDLE_SORTED_ASC, sortedAsc)
    }

    private fun setupListeners() {
        binding.fabClear.setOnClickListener {
            adapter.submitList(standings.clear())
        }

        binding.btnSort.setOnClickListener {
            sortedAsc = !sortedAsc
            if (sortedAsc) {
                binding.btnSort.text = getString(R.string.btn_sort_desc)
                adapter.submitList(standings.listOfTeamsAsc)
            } else {
                binding.btnSort.text = getString(R.string.btn_sort_asc)
                adapter.submitList(standings.listOfTeamsDesc)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = TeamAdapter()
        adapter.submitList(standings.listOfTeamsDesc)
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.rvGames)
        recyclerView.adapter = adapter
    }

    companion object {
        private const val BUNDLE_SORTED_ASC = "sorted_asc"

        @JvmStatic
        fun newInstance() = StandingsFragment()
    }
}