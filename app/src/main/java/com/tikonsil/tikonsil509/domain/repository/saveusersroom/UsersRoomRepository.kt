package com.tikonsil.tikonsil509.domain.repository.saveusersroom

import androidx.lifecycle.LiveData
import com.tikonsil.tikonsil509.data.local.dao.UsersDao
import com.tikonsil.tikonsil509.data.local.entity.UsersEntity

/** * Created by ISMOY BELIZAIRE on 30/04/2022. */
class UsersRoomRepository(private val usersDao: UsersDao) {
 val readAllData:LiveData<List<UsersEntity>> = usersDao.readAllData()
 suspend fun addUsers(usersEntity: UsersEntity){
  usersDao.addUsers(usersEntity)
 }


}