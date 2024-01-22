package com.ets.preduzmi.ui.your_posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ets.preduzmi.R
import com.ets.preduzmi.databinding.FragmentYourPostsBinding

class YourPostsFragment : Fragment() {
    private val viewModel: YourPostsViewModel by activityViewModels()

    private var _binding: FragmentYourPostsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourPostsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_your_posts_to_new_post)
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (viewModel.businesses.value == null) {
            binding.layoutSwipeRefresh.isRefreshing = true
            viewModel.loadBusinesses()
        }

        binding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.loadBusinesses()
        }

        viewModel.businesses.observe(viewLifecycleOwner) {
            binding.layoutSwipeRefresh.isRefreshing = false

            binding.recycler.adapter = YourPostsRecyclerViewAdapter(
                requireContext(),
                it,
                { business ->
                    val action = YourPostsFragmentDirections.actionYourPostsToBusinessDetails(business)
                    findNavController().navigate(action)
                },
                { business ->
                    val action = YourPostsFragmentDirections.actionYourPostsToEditPost(business)
                    findNavController().navigate(action)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}