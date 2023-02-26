package com.tikonsil.tikonsil509.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tikonsil.tikonsil509.data.local.entity.UsersEntity

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
  @Delete
  suspend fun deleteonlyuser(usesersEntity: UsersEntity)

  @Query("DELETE FROM users_table")
  suspend fun deleteAllUser()
}