package com.tikonsil.tikonsil509.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.register.RegisterRepository

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class RegisterViewModelFactory(private val repository: RegisterRepository):ViewModelProvider.Factory {
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
  return RegisterViewModel(repository) as T
 }

}