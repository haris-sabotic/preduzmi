package com.ets.preduzmi.ui.business_details

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ets.preduzmi.R
import com.ets.preduzmi.databinding.FragmentBusinessDetailsBinding
import com.ets.preduzmi.util.likeBusiness
import com.ets.preduzmi.util.saveBusiness

class BusinessDetailsFragment : Fragment() {
    private val args: BusinessDetailsFragmentArgs by navArgs()

    private var _binding: FragmentBusinessDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val business = args.businessModel

        binding.imageButtonBack.setOnClickListener { findNavController().popBackStack() }

        binding.textName.text = business.name
        binding.textDescription.text = business.description
        binding.textType.text = business.type
        binding.textTypeBelowName.text = business.type
        binding.textLegalType.text = business.legalType

        binding.textAuthor.text = business.postedBy.name
        binding.textPhone.text = business.postedBy.phone

        val imageByteArray: ByteArray = Base64.decode(business.photo, Base64.DEFAULT)
        Glide.with(requireContext())
            .asBitmap()
            .load(imageByteArray)
            .into(binding.image);

        val profileImageByteArray: ByteArray = Base64.decode(business.postedBy.photo, Base64.DEFAULT)
        Glide.with(requireContext())
            .asBitmap()
            .load(profileImageByteArray)
            .placeholder(R.drawable.default_user)
            .into(binding.profileImage);

        binding.buttonLike.setOnClickListener {
            likeBusiness(
                business.id,
                {
                    Toast.makeText(requireContext(), "Liked ${business.name}", Toast.LENGTH_SHORT).show()
                },
                { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.buttonSave.setOnClickListener {
            saveBusiness(
                business.id,
                {
                    Toast.makeText(requireContext(), "Saved ${business.name}", Toast.LENGTH_SHORT).show()
                },
                { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}