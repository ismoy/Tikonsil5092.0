package com.tikonsil.tikonsil509.domain.repository.savestatus

import androidx.lifecycle.LiveData
import com.tikonsil.tikonsil509.data.local.savestatus.StatusDao
import com.tikonsil.tikonsil509.data.local.savestatus.StatusUser

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
class StatusUserRepository(private val statusDao: StatusDao) {
    val readAllData: LiveData<List<StatusUser>> = statusDao.readAllData()
    suspend fun addStatus(statusUser: StatusUser){
        statusDao.addStatusUser(statusUser)
    }

    suspend fun deleteonlyUser(statusUser: StatusUser){
        statusDao.deleteonlyuser(statusUser)
    }

    suspend fun deleteAll(){
        statusDao.deleteAllUser()
    }
}