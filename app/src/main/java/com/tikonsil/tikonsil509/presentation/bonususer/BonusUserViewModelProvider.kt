package com.tikonsil.tikonsil509.presentation.bonususer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.bonususer.BonusUserRepository

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
class BonusUserViewModelProvider(private val repository: BonusUserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BonusUserViewModel(repository) as T
    }
}