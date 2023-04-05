package com.tikonsil.tikonsil509.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.Users
import com.tikonsil.tikonsil509.domain.repository.register.RegisterRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class RegisterViewModel(private var repository: RegisterRepository):ViewModel() {

 private val _responseRegister:MutableLiveData<Response<Users>> = MutableLiveData()
 val responseRegister:LiveData<Response<Users>> = _responseRegister
 val isLoading:MutableLiveData<Boolean> = MutableLiveData()
 fun register(uidUser:String,users: Users){
  isLoading.value = true
  viewModelScope.launch {
   val response = repository.register(uidUser,users)
   _responseRegister.value = response
  }
 }
}