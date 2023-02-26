package com.tikonsil.tikonsil509.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tikonsil.tikonsil509.data.local.entity.PriceRechargeAccountMaster

@Dao
interface PriceRechargeAccountMasterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(priceRechargeAccountMaster: PriceRechargeAccountMaster)

    @Query("SELECT * FROM PriceRechargeAccountMaster")
    suspend fun getAllPrices():List<PriceRechargeAccountMaster>

    @Query("DELETE FROM PriceRechargeAccountMaster")
    suspend fun deleteAllPrices():Int
}