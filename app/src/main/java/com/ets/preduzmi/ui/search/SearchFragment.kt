package com.ets.preduzmi.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ets.preduzmi.api.responses.SearchResponse
import com.ets.preduzmi.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by activityViewModels()

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var searchResults: SearchResponse = SearchResponse(arrayListOf(), arrayListOf())

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittext.editText!!.setText(viewModel.query)

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.results.observe(viewLifecycleOwner) {
            searchResults.businesses = it.businesses
            searchResults.users = it.users
            binding.recycler.adapter?.notifyDataSetChanged()
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = SearchRecyclerViewAdapter(
            requireContext(),
            searchResults,
            { businessModel ->
                val action = SearchFragmentDirections.actionSearchToBusinessDetails(businessModel)
                findNavController().navigate(action)
            },
            { userModel ->
            },
        )

        binding.edittext.editText!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = binding.edittext.editText!!.text.toString()

                if (text.isNotEmpty()) {
                    viewModel.search(text)

                    return@setOnEditorActionListener true
                }
            }

            false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}