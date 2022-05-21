package com.tikonsil.tikonsil509.data.local.savestatus

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
/**
 * Todo(@Database)
 * Contains the database holder and servers as the main access point
 * for the underlying connection to your app's data.
 */

@Database(entities = [StatusUser::class], version = 1, exportSchema = false)
abstract class StatusDatabase: RoomDatabase() {

    abstract fun statusDao(): StatusDao

    companion object {
        @Volatile
        private var INSTANCE: StatusDatabase?= null

        fun getDatabase(context: Context): StatusDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null ) {
                return tempInstance

            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StatusDatabase::class.java,
                    "status_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }

}