package com.tikonsil.tikonsil509.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.login.LoginRepository

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class LoginViewModelFactory(private val repository: LoginRepository):ViewModelProvider.Factory {
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
  return LoginViewModel(repository) as T
 }

}