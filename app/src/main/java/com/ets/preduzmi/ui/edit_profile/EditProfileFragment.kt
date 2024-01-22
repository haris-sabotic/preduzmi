package com.ets.preduzmi.ui.edit_profile

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
import com.ets.preduzmi.api.requests.EditUserRequest
import com.ets.preduzmi.databinding.FragmentEditProfileBinding
import com.ets.preduzmi.util.GlobalData
import java.io.ByteArrayOutputStream

class EditProfileFragment : Fragment() {
    private val args: EditProfileFragmentArgs by navArgs()
    private val viewModel: EditProfileViewModel by viewModels()

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

    private var _binding: FragmentEditProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = args.userModel


        binding.imageButtonBack.setOnClickListener { findNavController().popBackStack() }
        binding.buttonCancel.setOnClickListener { findNavController().popBackStack() }

        viewModel.user.observe(viewLifecycleOwner) { findNavController().popBackStack() }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.buttonEditPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.edittextName.editText!!.setText(user.name)
        binding.edittextPhone.editText!!.setText(user.phone)
        if (user.type == "Investitor") {
            binding.radiogroup.check(R.id.radiobutton_investitor)
        } else {
            binding.radiogroup.check(R.id.radiobutton_preduzetnik)
        }

        val imageByteArray: ByteArray = Base64.decode(user.photo, Base64.DEFAULT)
        Glide.with(requireContext())
            .asBitmap()
            .load(imageByteArray)
            .placeholder(R.drawable.default_user)
            .into(binding.photo);


        binding.buttonSave.setOnClickListener {
            val photo = selectedPhotoString
            val name = binding.edittextName.editText!!.text.toString()
            val type = if (binding.radiogroup.checkedRadioButtonId == R.id.radiobutton_investitor) "Investitor" else "Preduzetnik"
            val phone = binding.edittextPhone.editText!!.text.toString()

            if (name.isNotEmpty() && type.isNotEmpty() && phone.isNotEmpty()) {
                viewModel.editProfile(
                    EditUserRequest(name, type, phone, photo)
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