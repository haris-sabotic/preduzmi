package com.ets.preduzmi.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ets.preduzmi.R
import com.ets.preduzmi.databinding.FragmentLoginBinding
import com.ets.preduzmi.util.GlobalData

class LoginFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.token.observe(viewLifecycleOwner) { token ->
            binding.progress.visibility = View.GONE

            if (binding.checkboxRememberMe.isChecked) {
                GlobalData.saveToken(requireContext(), token)
            } else {
                GlobalData.setToken(token)
            }

            findNavController().navigate(R.id.action_login_to_home)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.progress.visibility = View.GONE

            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        binding.buttonSubmit.setOnClickListener {
            val email = binding.edittextEmail.editText!!.text.toString()
            val password = binding.edittextPassword.editText!!.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.progress.visibility = View.VISIBLE
                viewModel.login(email, password)
            }
        }

        binding.edittextEmail.editText!!.addTextChangedListener { updateButtonTint() }
        binding.edittextPassword.editText!!.addTextChangedListener { updateButtonTint() }

        binding.textRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun updateButtonTint() {
        val email = binding.edittextEmail.editText!!.text.toString()
        val password = binding.edittextPassword.editText!!.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            binding.buttonSubmit.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.disabled_button)
        } else {
            binding.buttonSubmit.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_500)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}