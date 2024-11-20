package com.example.tripplannerdemo.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.tripplannerdemo.R
import com.example.tripplannerdemo.databinding.FragmentSigninBinding
import com.example.tripplannerdemo.ui.signin.viewmodel.SignInNavigator
import com.example.tripplannerdemo.ui.signin.viewmodel.SigninViewModel
import org.koin.android.ext.android.inject

class SigninFragment : Fragment(), SignInNavigator {

    private lateinit var binding : FragmentSigninBinding
    private val viewModel: SigninViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigator(this)
        binding.btnSignIn.setOnClickListener{
            viewModel.signInIntoAccount(
            binding.etEmail.text.toString().trim()
            ,binding.etPassword.text.toString().trim())
        }
        binding.tvSignUp.setOnClickListener {
            // Navigate to Sign-Up screen
            findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
        }
    }

    override fun onLoginSuccess() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.signinFragment, true)
            .build()
        findNavController().navigate(R.id.homeFragment,null,options)
    }

    override fun onLoginFailure(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onEmailError(error: String) {
        binding.etEmail.error = error
    }

    override fun onPasswordError(error: String) {
        binding.etPassword.error = error
    }
}