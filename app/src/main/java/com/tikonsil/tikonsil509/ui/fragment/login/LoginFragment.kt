package com.tikonsil.tikonsil509.ui.fragment.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentLoginBinding
import com.tikonsil.tikonsil509.presentation.login.LoginViewModel
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.STATUSUSERS

class LoginFragment : ValidateLogin<LoginViewModel, FragmentLoginBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        validateRealTime()
        binding.iralregistro.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            validateOnclickButton()
        }
        clickwhatsapp()
    }
    override fun getViewModel()= LoginViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?)= FragmentLoginBinding.inflate(inflater,container,false)

}