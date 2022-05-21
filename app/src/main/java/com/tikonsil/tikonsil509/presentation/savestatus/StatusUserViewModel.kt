package com.tikonsil.tikonsil509.presentation.savestatus

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.data.local.savestatus.StatusDatabase
import com.tikonsil.tikonsil509.data.local.savestatus.StatusUser
import com.tikonsil.tikonsil509.domain.repository.savestatus.StatusUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
class StatusUserViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<StatusUser>>
    private val repository: StatusUserRepository
    init {
        val statusDao = StatusDatabase.getDatabase(application).statusDao()
        repository = StatusUserRepository(statusDao)
        readAllData = repository.readAllData
    }
    fun addstatuss(productsEntity: StatusUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addStatus(productsEntity)
        }
    }
    fun deleteonlyUser(statusUser: StatusUser){
        viewModelScope.launch(Dispatchers.IO ) {
            repository.deleteonlyUser(statusUser)
        }
    }
    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}