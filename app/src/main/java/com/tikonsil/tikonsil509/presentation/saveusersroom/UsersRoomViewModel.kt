package com.tikonsil.tikonsil509.presentation.saveusersroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.data.local.db.UsersDatabase
import com.tikonsil.tikonsil509.data.local.db.UsersEntity
import com.tikonsil.tikonsil509.domain.repository.saveusersroom.UsersRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** * Created by ISMOY BELIZAIRE on 30/04/2022. */
class UsersRoomViewModel(application: Application):AndroidViewModel(application){
 val readAllData:LiveData<List<UsersEntity>>
  private val repository:UsersRoomRepository
  init {
      val usersDao =UsersDatabase.getDatabase(application).userDao()
   repository = UsersRoomRepository(usersDao)
   readAllData = repository.readAllData
  }
 fun addUsers(productsEntity: UsersEntity) {
  viewModelScope.launch(Dispatchers.IO) {
   repository.addUsers(productsEntity)
  }
 }

}