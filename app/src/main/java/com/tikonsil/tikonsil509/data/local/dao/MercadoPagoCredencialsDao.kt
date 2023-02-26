package com.tikonsil.tikonsil509.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tikonsil.tikonsil509.data.local.entity.MercadoPagoCredencials

@Dao
interface MercadoPagoCredencialsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetCredential(vararg mercadoPagoCredencials: MercadoPagoCredencials)

    @Query("Delete From MercadoPagoCredencials")
    suspend fun deleteAll():Int

    @Query("Select * From MercadoPagoCredencials")
    suspend fun getAllData():List<MercadoPagoCredencials>
}