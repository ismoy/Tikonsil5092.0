package com.tikonsil.tikonsil509.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.databinding.FragmentLoginBinding
import com.tikonsil.tikonsil509.presentation.login.LoginViewModel
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.utils.constants.Constant
import com.tikonsil.tikonsil509.utils.constants.UtilsView

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mConstant: Constant
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var navController: NavController
    private lateinit var mUserProvider: UserProvider

    private val viewModel by lazy { ViewModelProvider(requireActivity())[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = Navigation.findNavController(view)
        mAuthProvider = AuthProvider()
        mConstant = Constant()
        mUserProvider = UserProvider()
        binding.iralregistro.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        validateRealTime()
        clickWhatsapp()

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        viewModelObserver()
        binding.forgotPassword.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }


    private fun loginUser() {
        when {
            binding.emaillogin.text.toString().isNotEmpty() && binding.passwordlogin.text.toString()
                .isNotEmpty()->{
                login(binding.emaillogin.text.toString(),binding.passwordlogin.text.toString())
            }
            else -> {
                binding.layoutpasswordlogin.helperText = context?.getString(R.string.erroremptyfield)
            }
        }
    }

    private fun login(email: String , password: String) {
        viewModel.login(email,password)
    }
    private fun viewModelObserver() {
        viewModel._responseLogin.observe(requireActivity()) { task ->
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    if (mAuthProvider.isEmailVerified() == true) {
                        UtilsView.hideProgress(binding.btnLogin,binding.progressBar,requireActivity().getString(R.string.singin))
                        val intent = Intent(requireContext() , HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            requireContext() ,
                            getString(R.string.verifyaccount) ,
                            Toast.LENGTH_LONG
                        ).show()
                        UtilsView.hideProgress(binding.btnLogin,binding.progressBar,requireActivity().getString(R.string.singin))
                    }
                } else {
                    Toast.makeText(requireContext() , it.exception?.message , Toast.LENGTH_LONG)
                        .show()
                    UtilsView.hideProgress(binding.btnLogin,binding.progressBar,requireActivity().getString(R.string.singin))
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){
            UtilsView.showProgress(binding.btnLogin,binding.progressBar)
        }
    }


    private fun clickWhatsapp() {
        UtilsView.supportWhatsapp(binding.whatsapp , requireActivity())
    }

    private fun validateRealTime() {
        with(binding) {
            UtilsView.loginValidator(
                layoutemaillogin , emaillogin , layoutpasswordlogin , passwordlogin ,
                requireContext() , mConstant
            )
        }

    }

}