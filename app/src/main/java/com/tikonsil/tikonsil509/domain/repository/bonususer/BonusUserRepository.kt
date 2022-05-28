package com.tikonsil.tikonsil509.domain.repository.bonususer

import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.BonusUser
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
class BonusUserRepository {

 suspend fun getBonusUser():Response<BonusUser>{
  return RetrofitInstance.tikonsilApi.getBounusUser()
 }
}