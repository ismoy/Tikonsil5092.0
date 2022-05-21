package com.tikonsil.tikonsil509.data.local.savestatus

import androidx.room.Entity
import androidx.room.PrimaryKey

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
@Entity(tableName = "status_table")//Todo(the table name for database)
data class StatusUser (
    @PrimaryKey(autoGenerate = true)//Todo(generate id automatically)
    val id: Int,
    val status:Int
    )