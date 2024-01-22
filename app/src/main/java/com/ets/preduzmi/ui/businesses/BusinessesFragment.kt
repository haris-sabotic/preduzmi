package com.ets.preduzmi.ui.businesses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ets.preduzmi.databinding.FragmentBusinessesBinding
import com.ets.preduzmi.util.likeBusiness
import com.ets.preduzmi.util.saveBusiness

class BusinessesFragment : Fragment() {
    private val viewModel: BusinessesViewModel by activityViewModels()

    private var _binding: FragmentBusinessesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedFiltersType: ArrayList<String>? = null
    private var selectedFiltersLegalType: ArrayList<String>? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.imageButtonFilters.setOnClickListener { binding.layoutFilters.visibility = View.VISIBLE }
        binding.filtersImgClose.setOnClickListener { binding.layoutFilters.visibility = View.GONE }
        binding.filtersButtonCancel.setOnClickListener { binding.layoutFilters.visibility = View.GONE }
        binding.filtersButtonApply.setOnClickListener {
            val types: ArrayList<String> = arrayListOf()
            val legalTypes: ArrayList<String> = arrayListOf()

            binding.filtersLayoutType.children.forEach { view ->
                val checkbox = view as CheckBox
                if (checkbox.isChecked) {
                    Log.d("CHECKED", checkbox.text.toString())
                    types.add(checkbox.text.toString())
                } else {
                    Log.d("NOT CHECKED", checkbox.text.toString())
                }
            }

            binding.filtersLayoutLegalType.children.forEach { view ->
                val checkbox = view as CheckBox
                if (checkbox.isChecked) {
                    Log.d("CHECKED", checkbox.text.toString())
                    legalTypes.add(checkbox.text.toString())
                } else {
                    Log.d("NOT CHECKED", checkbox.text.toString())
                }
            }

            selectedFiltersType = if (types.isEmpty()) null else types
            selectedFiltersLegalType = if (legalTypes.isEmpty()) null else legalTypes
            binding.layoutFilters.visibility = View.GONE
            binding.layoutSwipeRefresh.isRefreshing = true
            viewModel.loadBusinesses(selectedFiltersType, selectedFiltersLegalType)
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (viewModel.businesses.value == null) {
            binding.layoutSwipeRefresh.isRefreshing = true
            viewModel.loadBusinesses(selectedFiltersType, selectedFiltersLegalType)
        }

        binding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.loadBusinesses(selectedFiltersType, selectedFiltersLegalType)
        }

        viewModel.businesses.observe(viewLifecycleOwner) {
            binding.layoutSwipeRefresh.isRefreshing = false

            binding.recycler.adapter = BusinessesRecyclerViewAdapter(
                requireContext(),
                it,
                { business ->
                    val action = BusinessesFragmentDirections.actionHomeToBusinessDetails(business)
                    findNavController().navigate(action)
                },
                { business ->
                    likeBusiness(
                        business.id,
                        {
                            Toast.makeText(requireContext(), "Liked ${business.name}", Toast.LENGTH_SHORT).show()
                        },
                        { message ->
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                { business ->
                    saveBusiness(
                        business.id,
                        {
                            Toast.makeText(requireContext(), "Saved ${business.name}", Toast.LENGTH_SHORT).show()
                        },
                        { message ->
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}