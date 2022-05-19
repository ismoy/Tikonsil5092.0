package com.tikonsil.tikonsil509.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** * Created by ISMOY BELIZAIRE on 30/04/2022. */
/**
 * Todo(@Database)
 * Contains the database holder and servers as the main access point
 * for the underlying connection to your app's data.
 */

@Database(entities = [UsersEntity::class], version = 1, exportSchema = false)
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

}