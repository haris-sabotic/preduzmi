package com.ets.preduzmi.ui.edit_post

import android.graphics.Bitmap
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ets.preduzmi.R
import com.ets.preduzmi.api.requests.EditBusinessRequest
import com.ets.preduzmi.databinding.FragmentEditPostBinding
import com.ets.preduzmi.util.GlobalData
import java.io.ByteArrayOutputStream

class EditPostFragment : Fragment() {
    private val args: EditPostFragmentArgs by navArgs()
    private val viewModel: EditPostViewModel by viewModels()

    private var selectedPhotoString: String? = null

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)

            selectedPhotoString = Base64.encodeToString(
                getBytesFromBitmap(bitmap),
                Base64.NO_WRAP
            )

            Glide.with(requireContext())
                .asBitmap()
                .load(bitmap)
                .into(binding.photo);
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private var _binding: FragmentEditPostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val business = args.businessModel


        binding.imageButtonBack.setOnClickListener { findNavController().popBackStack() }
        binding.buttonCancel.setOnClickListener { findNavController().popBackStack() }

        viewModel.business.observe(viewLifecycleOwner) { findNavController().popBackStack() }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.buttonEditPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.edittextName.editText!!.setText(business.name)
        binding.edittextDescription.editText!!.setText(business.description)

        binding.dropdownType.editText!!.setText(business.type)
        binding.dropdownLegalType.editText!!.setText(business.legalType)

        val imageByteArray: ByteArray = Base64.decode(business.photo, Base64.DEFAULT)
        Glide.with(requireContext())
            .asBitmap()
            .load(imageByteArray)
            .into(binding.photo);

        val legalTypesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, GlobalData.BUSINESS_LEGAL_TYPES)
        (binding.dropdownLegalType.editText as? AutoCompleteTextView)?.setAdapter(legalTypesAdapter)

        val typesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, GlobalData.BUSINESS_TYPES)
        (binding.dropdownType.editText as? AutoCompleteTextView)?.setAdapter(typesAdapter)


        binding.buttonSubmit.setOnClickListener {
            val photo = selectedPhotoString
            val name = binding.edittextName.editText!!.text.toString()
            val type = binding.dropdownType.editText!!.text.toString()
            val legalType = binding.dropdownLegalType.editText!!.text.toString()
            val description = binding.edittextDescription.editText!!.text.toString()

            if (name.isNotEmpty() && type.isNotEmpty() && legalType.isNotEmpty() && description.isNotEmpty()) {
                viewModel.editBusiness(
                    business.id,
                    EditBusinessRequest(name, type, legalType, description, photo)
                )
            }
        }
    }

    private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
        return stream.toByteArray()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}