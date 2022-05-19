package com.tikonsil.tikonsil509.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/** * Created by ISMOY BELIZAIRE on 30/04/2022. */
/**
 * Todo(@Dao)
 * DAOs are responsible for defining the methods that access the
 * database.This is the place where we write our queries.
 */
@Dao
interface UsersDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun addUsers(usesersEntity: UsersEntity)

  @Query("SELECT * FROM users_table ORDER BY id ASC")
  fun readAllData(): LiveData<List<UsersEntity>>
}