package com.tikonsil.tikonsil509.presentation.resetPassword

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.tikonsil.tikonsil509.domain.repository.resetPassword.ResetPasswordRepository
import kotlinx.coroutines.launch

class ResetPasswordViewModel:ViewModel() {

    private val repository:ResetPasswordRepository=ResetPasswordRepository()
     val resetPassword:MutableLiveData<Task<Void>> by lazy { MutableLiveData() }
    val loading by lazy { MutableLiveData<Boolean>() }


    fun resetPassword(email:String,context: Context){
        loading.value =true
        viewModelScope.launch {
            val response = repository.resetPassword(email,context)
            resetPassword.postValue(response)
        }
    }
}