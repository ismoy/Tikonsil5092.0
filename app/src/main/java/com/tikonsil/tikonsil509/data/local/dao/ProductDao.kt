package com.tikonsil.tikonsil509.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tikonsil.tikonsil509.data.local.entity.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(vararg product: Product)

    @Query("Delete From Product")
    suspend fun deleteAll():Int

    @Query("Select * From Product")
    suspend fun getAllData():List<Product>
}