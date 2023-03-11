package com.tikonsil.tikonsil509.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.Users
import com.tikonsil.tikonsil509.domain.repository.home.UsersRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
class UserViewModel :ViewModel() {
 private val repository:UsersRepository =UsersRepository()
 val ResposeUsers:MutableLiveData<Response<Users>> = MutableLiveData()

 fun getOnlyUser(uidUser:String){
  viewModelScope.launch {
   val response = repository.getOnlyUser(uidUser)
   ResposeUsers.value = response
  }
 }

}