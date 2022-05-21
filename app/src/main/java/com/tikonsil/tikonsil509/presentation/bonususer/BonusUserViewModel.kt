package com.tikonsil.tikonsil509.presentation.bonususer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.BonusUser
import com.tikonsil.tikonsil509.domain.repository.bonususer.BonusUserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
class BonusUserViewModel(private val repository: BonusUserRepository):ViewModel() {
 val myResponseGetUserBonus: MutableLiveData<Response<BonusUser>> by lazy { MutableLiveData() }

 fun getBonusUser() {
  viewModelScope.launch {
   val response = repository.getBonusUser()
   myResponseGetUserBonus.value = response
  }
 }
}