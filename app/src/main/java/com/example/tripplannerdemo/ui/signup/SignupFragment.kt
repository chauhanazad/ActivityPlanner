package com.example.tripplannerdemo.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.tripplannerdemo.R
import com.example.tripplannerdemo.databinding.FragmentSignupBinding
import com.example.tripplannerdemo.ui.signup.viewmodel.SignUpNavigator
import com.example.tripplannerdemo.ui.signup.viewmodel.SignupViewModel
import org.koin.android.ext.android.inject

class SignupFragment : Fragment(), SignUpNavigator{

    private lateinit var binding : FragmentSignupBinding
    private val viewModel: SignupViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigator(this)

        binding.btnSignUp.setOnClickListener{
            viewModel.registerUser(binding.etName.text.toString().trim()
            ,binding.etEmail.text.toString().trim()
            ,binding.etPassword.text.toString().trim()
            ,binding.etRePassword.text.toString().trim())
        }
    }

    override fun onEmailError(error: String) {
        binding.etEmail.error = error
    }

    override fun onPasswordError(error: String) {
        binding.etPassword.error = error
    }

    override fun onReEnterPasswordError(error: String) {
        binding.etRePassword.error = error
    }

    override fun onNameError(error: String) {
        binding.etName.error=error
    }

    override fun onSignupSuccess() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.signupFragment,true)
            .build()
        findNavController().navigate(R.id.homeFragment,null,options)
    }

    override fun onSignupFailure(error: String) {
        Toast.makeText(requireContext(),error,Toast.LENGTH_SHORT).show()
    }
}