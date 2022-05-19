package com.tikonsil.tikonsil509.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.home.UsersRepository

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
class UserViewModelFactory(private val repository: UsersRepository):ViewModelProvider.Factory {
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
  return UserViewModel(repository) as T
 }

}