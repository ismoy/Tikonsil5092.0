package com.tikonsil.tikonsil509.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.tikonsil.tikonsil509.domain.repository.login.LoginRepository
import kotlinx.coroutines.launch

/** * Created by ISMOY BELIZAIRE on 26/04/2022. */
class LoginViewModel:ViewModel() {
 private val repository:LoginRepository = LoginRepository()
  val _responseLogin: MutableLiveData<Task<AuthResult>> = MutableLiveData()
 val loading by lazy { MutableLiveData<Boolean>() }
 fun login(email:String,password:String){
  loading.value = true
  viewModelScope.launch {
   val response = repository.login(email, password)
   _responseLogin.postValue(response)
  }
 }

}
