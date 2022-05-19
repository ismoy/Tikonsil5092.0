package com.tikonsil.tikonsil509.presentation.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.Users
import com.tikonsil.tikonsil509.domain.repository.register.RegisterRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class RegisterViewModel(private var repository: RegisterRepository):ViewModel() {
 val responseRegister:MutableLiveData<Response<Users>> = MutableLiveData()

 fun register(uidUser:String,users: Users){
  viewModelScope.launch {
   val response = repository.register(uidUser,users)
   responseRegister.value = response
  }
 }
}