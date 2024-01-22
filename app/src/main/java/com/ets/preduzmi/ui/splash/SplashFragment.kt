package com.ets.preduzmi.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ets.preduzmi.databinding.FragmentSplashBinding
import androidx.navigation.fragment.findNavController
import com.ets.preduzmi.R
import com.ets.preduzmi.util.GlobalData

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Handler(Looper.getMainLooper()).postDelayed({
            GlobalData.loadTokenFromSharedPrefs(requireContext())
            if (GlobalData.getToken() == null) {
                findNavController().navigate(R.id.action_splash_to_login)
            } else {
                findNavController().navigate(R.id.action_splash_to_home)
            }
        }, 1000)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}