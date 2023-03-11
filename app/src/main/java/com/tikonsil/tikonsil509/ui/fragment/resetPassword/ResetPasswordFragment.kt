package com.tikonsil.tikonsil509.ui.fragment.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentResetPasswordBinding
import com.tikonsil.tikonsil509.presentation.resetPassword.ResetPasswordViewModel
import com.tikonsil.tikonsil509.utils.constants.Constant
import com.tikonsil.tikonsil509.utils.constants.UtilsView

class ResetPasswordFragment : Fragment() {

    private lateinit var binding:FragmentResetPasswordBinding
    private val mConstant by lazy { Constant() }
    private val viewModel by lazy { ViewModelProvider(requireActivity())[ResetPasswordViewModel::class.java] }
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentResetPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = Navigation.findNavController(view)
        validateRealtime()
        binding.btnRecoverPassword.setOnClickListener(resetPassword())
        binding.arrowbackregister.setOnClickListener(goToHome())
        observeViewModel()
    }

    private fun goToHome(): View.OnClickListener {
        return View.OnClickListener {
            navController.navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }

    }

    private fun observeViewModel() {
        viewModel.resetPassword.observe(viewLifecycleOwner){task->
            task.addOnCompleteListener {isComplete->
                if (isComplete.isSuccessful){
                    Toast.makeText(
                        requireContext() ,
                        "Se ha enviado un correo electrónico para restablecer su contraseña revise su bandeja de entrada" ,
                        Toast.LENGTH_LONG
                    ).show()
                    UtilsView.hideProgress(binding.btnRecoverPassword,binding.progressBar,requireActivity().getString(R.string.recover_password))
                    navController.navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                }else{
                    Toast.makeText(
                        requireContext() ,
                        "No se pudo enviar el correo electrónico para restablecer la contraseña" ,
                        Toast.LENGTH_LONG
                    ).show()
                    UtilsView.hideProgress(binding.btnRecoverPassword,binding.progressBar,requireActivity().getString(R.string.recover_password))

                }
            }
        }
        viewModel.loading.observe(viewLifecycleOwner){
            UtilsView.showProgress(binding.btnRecoverPassword,binding.progressBar)
        }

    }

    private fun resetPassword(): View.OnClickListener {
        return View.OnClickListener {
            if (binding.emaillogin.text.toString().isNotEmpty()){
                sendEmailForResetPassword(binding.emaillogin.text.toString())
            }else{
                binding.layoutemaillogin.helperText =requireActivity().getString(R.string.erroremptyfield)
            }
        }

    }

    private fun sendEmailForResetPassword(email: String) {
        viewModel.resetPassword(email,requireContext())

    }

    private fun validateRealtime() {
        UtilsView.loginValidator(binding.layoutemaillogin,binding.emaillogin,binding.layoutemaillogin,
        binding.emaillogin,requireContext(),mConstant)
    }

}