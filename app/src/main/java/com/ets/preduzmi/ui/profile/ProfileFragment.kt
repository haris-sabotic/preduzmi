package com.ets.preduzmi.ui.profile

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ets.preduzmi.R
import com.ets.preduzmi.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.backgroundEdit.setOnClickListener {
            viewModel.userData.value?.let { user ->
                val action = ProfileFragmentDirections.actionProfileToEditProfile(user)
                findNavController().navigate(action)
            }
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // load likedBusinesses at start
        if (viewModel.likedBusinesses.value == null) {
            viewModel.loadLikedBusinesses()
            setLikedLayoutAppearance(true)
            setSavedLayoutAppearance(false)
        }

        binding.layoutLiked.setOnClickListener {
            setLikedLayoutAppearance(true)
            setSavedLayoutAppearance(false)
            viewModel.loadLikedBusinesses()
            binding.layoutSwipeRefresh.isRefreshing = true
        }

        binding.layoutSaved.setOnClickListener {
            setLikedLayoutAppearance(false)
            setSavedLayoutAppearance(true)
            viewModel.loadSavedBusinesses()
            binding.layoutSwipeRefresh.isRefreshing = true
        }

        binding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.loadUserData()
        }

        viewModel.likedBusinesses.observe(viewLifecycleOwner) {
            setLikedLayoutAppearance(true)
            setSavedLayoutAppearance(false)

            if (viewModel.userData.value != null) {
                binding.layoutSwipeRefresh.isRefreshing = false
            }

            binding.recycler.adapter = ProfileRecyclerViewAdapter(
                requireContext(),
                it,
            ) { business ->
                val action = ProfileFragmentDirections.actionProfileToBusinessDetails(business)
                findNavController().navigate(action)
            }
        }

        viewModel.savedBusinesses.observe(viewLifecycleOwner) {
            setLikedLayoutAppearance(false)
            setSavedLayoutAppearance(true)

            if (viewModel.userData.value != null) {
                binding.layoutSwipeRefresh.isRefreshing = false
            }

            binding.recycler.adapter = ProfileRecyclerViewAdapter(
                requireContext(),
                it,
            ) { business ->
                val action = ProfileFragmentDirections.actionProfileToBusinessDetails(business)
                findNavController().navigate(action)
            }
        }

        if (viewModel.userData.value == null) {
            binding.layoutSwipeRefresh.isRefreshing = true
            viewModel.loadUserData()
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            binding.layoutSwipeRefresh.isRefreshing = false

            binding.textName.text = it.name

            val photoByteArray: ByteArray = Base64.decode(it.photo, Base64.DEFAULT)

            Glide.with(requireContext())
                .asBitmap()
                .load(photoByteArray)
                .placeholder(R.drawable.default_user)
                .into(binding.imagePhoto);
        }
    }

    private fun setLikedLayoutAppearance(selected: Boolean) {
        val color = if (selected) R.color.purple_500 else R.color.gray_icon

        binding.textLiked.setTextColor(resources.getColor(color))
        binding.imageLiked.imageTintList = ContextCompat.getColorStateList(requireContext(), color)
        binding.bottomLineLiked.setBackgroundResource(color)
    }

    private fun setSavedLayoutAppearance(selected: Boolean) {
        val color = if (selected) R.color.purple_500 else R.color.gray_icon

        binding.textSaved.setTextColor(resources.getColor(color))
        binding.imageSaved.imageTintList = ContextCompat.getColorStateList(requireContext(), color)
        binding.bottomLineSaved.setBackgroundResource(color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}