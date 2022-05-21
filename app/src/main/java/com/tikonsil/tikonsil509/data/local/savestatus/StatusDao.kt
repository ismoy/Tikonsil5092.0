package com.tikonsil.tikonsil509.data.local.savestatus

import androidx.lifecycle.LiveData
import androidx.room.*

/** * Created by ISMOY BELIZAIRE on 30/04/2022. */
/**
 * Todo(@Dao)
 * DAOs are responsible for defining the methods that access the
 * database.This is the place where we write our queries.
 */
@Dao
interface StatusDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun addStatusUser(statususer: StatusUser)

  @Query("SELECT * FROM status_table ORDER BY id ASC")
  fun readAllData(): LiveData<List<StatusUser>>
  @Delete
  suspend fun deleteonlyuser(statususer: StatusUser)

  @Query("DELETE FROM status_table")
  suspend fun deleteAllUser()
}