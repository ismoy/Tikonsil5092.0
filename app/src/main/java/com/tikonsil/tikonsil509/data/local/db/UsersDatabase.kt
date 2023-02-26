package com.tikonsil.tikonsil509.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.tikonsil.tikonsil509.data.local.dao.MercadoPagoCredencialsDao
import com.tikonsil.tikonsil509.data.local.dao.PriceRechargeAccountMasterDao
import com.tikonsil.tikonsil509.data.local.dao.ProductDao
import com.tikonsil.tikonsil509.data.local.dao.UsersDao
import com.tikonsil.tikonsil509.data.local.entity.MercadoPagoCredencials
import com.tikonsil.tikonsil509.data.local.entity.PriceRechargeAccountMaster
import com.tikonsil.tikonsil509.data.local.entity.Product
import com.tikonsil.tikonsil509.data.local.entity.UsersEntity

/** * Created by ISMOY BELIZAIRE on 30/04/2022. */
/**
 * Todo(@Database)
 * Contains the database holder and servers as the main access point
 * for the underlying connection to your app's data.
 */

@Database(entities = [UsersEntity::class,MercadoPagoCredencials::class,Product::class,PriceRechargeAccountMaster::class], version = 15, exportSchema = false)
abstract class UsersDatabase: RoomDatabase() {

    abstract fun userDao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: UsersDatabase?= null

        fun getDatabase(context: Context): UsersDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null ) {
                return tempInstance

            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsersDatabase::class.java,
                    "users_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
    abstract fun mercadoPagoCredentialsDao(): MercadoPagoCredencialsDao
    abstract fun productDao(): ProductDao
    abstract fun priceRechargeAccountMasterDao():PriceRechargeAccountMasterDao

}