package com.ets.preduzmi.ui.new_post

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ets.preduzmi.R
import com.ets.preduzmi.databinding.FragmentNewPostBinding
import com.ets.preduzmi.util.GlobalData
import java.io.ByteArrayOutputStream


class NewPostFragment : Fragment() {
    private val viewModel: NewPostViewModel by viewModels()

    private var selectedPhotoString: String? = null

    val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)

            selectedPhotoString = Base64.encodeToString(
                getBytesFromBitmap(bitmap),
                Base64.NO_WRAP
            )

            binding.layoutUploadPhoto.visibility = View.GONE
            binding.photoSelectedParent.visibility = View.VISIBLE

            Glide.with(requireContext())
                .asBitmap()
                .load(bitmap)
                .into(binding.photoSelected);
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    private var _binding: FragmentNewPostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBack.setOnClickListener { findNavController().popBackStack() }
        binding.buttonCancel.setOnClickListener { findNavController().popBackStack() }

        viewModel.business.observe(viewLifecycleOwner) { findNavController().popBackStack() }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.layoutUploadPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        binding.buttonSubmit.setOnClickListener {
            val photo = selectedPhotoString
            val name = binding.edittextName.editText!!.text.toString()
            val type = binding.dropdownType.editText!!.text.toString()
            val legalType = binding.dropdownLegalType.editText!!.text.toString()
            val description = binding.edittextDescription.editText!!.text.toString()

            if (selectedPhotoString != null && name.isNotEmpty() && type.isNotEmpty() && legalType.isNotEmpty() && description.isNotEmpty()) {
                viewModel.createBusiness(name, type, legalType, description, photo!!)
            }
        }

        val legalTypesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, GlobalData.BUSINESS_LEGAL_TYPES)
        (binding.dropdownLegalType.editText as? AutoCompleteTextView)?.setAdapter(legalTypesAdapter)

        val typesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, GlobalData.BUSINESS_TYPES)
        (binding.dropdownType.editText as? AutoCompleteTextView)?.setAdapter(typesAdapter)
    }

    private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 60, stream)
        return stream.toByteArray()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}