package com.tikonsil.tikonsil509.ui.fragment.register

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.databinding.FragmentRegisterBinding
import com.tikonsil.tikonsil509.domain.model.Users
import com.tikonsil.tikonsil509.domain.repository.register.RegisterRepository
import com.tikonsil.tikonsil509.presentation.register.RegisterViewModel
import com.tikonsil.tikonsil509.presentation.register.RegisterViewModelFactory
import com.tikonsil.tikonsil509.utils.constants.Constant
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import com.tikonsil.tikonsil509.utils.constants.UtilsView.hideProgress
import com.tikonsil.tikonsil509.utils.constants.UtilsView.registerUserValidator
import com.tikonsil.tikonsil509.utils.constants.UtilsView.showProgress
import kotlinx.coroutines.launch

class RegisterFragment:Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController:NavController
    private lateinit var mConstant: Constant
    private var countrySelected: String? = null
    private var codeCountrySelected: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var viewmodel: RegisterViewModel
    private lateinit var mAuthProvider:AuthProvider
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mConstant = Constant()
        mAuthProvider = AuthProvider()
        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        val repository = RegisterRepository()
        val factory = RegisterViewModelFactory(repository)
        viewmodel = ViewModelProvider(requireActivity(),factory)[RegisterViewModel::class.java]
        binding.arrowbackregister.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
        with(binding) {
            registerUserValidator(layoutfirstname,firstname,layoutlastname,lastname,layoutemail,email,
            layoutphone,phone,layoutpassword,password,layoutconfirmpassword,repeatpassword,btnRegistrar,
            requireContext(),mConstant)
        }
        setCountryListener()
        binding.btnRegistrar.setOnClickListener(onClickCreateUser())
        onViewModelObserver()
    }

    private fun onViewModelObserver() {
        viewmodel.isLoading.observe(viewLifecycleOwner){
           showProgress(binding.btnRegistrar,binding.progressBar)
        }
        viewmodel.responseRegister.observe(viewLifecycleOwner){response->
            if (response.isSuccessful){
               hideProgress(binding.btnRegistrar,binding.progressBar,requireActivity().getString(R.string.register))
            }else{
                Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onClickCreateUser(): View.OnClickListener {
        return View.OnClickListener {
            with(binding) {
                if (firstname.text.toString().isNotEmpty() && lastname.text.toString().isNotEmpty() &&
                    email.text.toString().isNotEmpty() && phone.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() && repeatpassword.text.toString().isNotEmpty()){
                    showProgress(binding.btnRegistrar,binding.progressBar)
                    mAuthProvider.register(email.text.toString(),repeatpassword.text.toString()).addOnCompleteListener {task->
                        if (task.isSuccessful){
                            sendUserDataInRealTimeDataBase()
                            val user: FirebaseUser? = auth.currentUser
                            user?.sendEmailVerification()?.addOnCompleteListener { task1: Task<Void?>?->
                                if (task1?.isSuccessful!!){
                                    Toast.makeText(requireContext(), getString(R.string.succescreateaccount), Toast.LENGTH_SHORT).show()
                                    navController.navigate(R.id.action_registerFragment_to_loginFragment)
                                }
                            }?.addOnFailureListener { e:Exception->

                            }
                            mAuthProvider.language(requireContext())
                        }else{
                            layoutemail.helperText = getString(R.string.existemail)
                        }
                    }

                }else{
                    Toast.makeText(requireContext(), getString(R.string.erroremptyfield), Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun sendUserDataInRealTimeDataBase() {
        with(binding) {
            val users = Users(mAuthProvider.getId().toString(),countrySelected,codeCountrySelected,
                firstname.text.toString(),lastname.text.toString(),email.text.toString(),"${binding.codigo.text}${binding.phone.text.toString()}",
                1,password.text.toString(),repeatpassword.text.toString(),0F,0F,
                0F,0F,1,"")
            viewmodel.register(mAuthProvider.getId().toString(),users)
        }
    }

    private fun setCountryListener() {
        with(binding) {
            codeCountrySelected = buttonpaises.selectedCountryNameCode
            countrySelected = buttonpaises.selectedCountryName
            codigo.text = buttonpaises.selectedCountryCodeWithPlus
            buttonpaises.setOnCountryChangeListener {
                codeCountrySelected = buttonpaises.selectedCountryNameCode
                countrySelected = buttonpaises.selectedCountryName
                codigo.text = buttonpaises.selectedCountryCodeWithPlus
            }

        }

    }
}