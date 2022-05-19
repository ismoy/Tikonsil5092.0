package com.tikonsil.tikonsil509.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/** * Created by ISMOY BELIZAIRE on 30/04/2022. */
@Entity(tableName = "users_table")//Todo(the table name for database)
data class UsersEntity(
    @PrimaryKey(autoGenerate = true)//Todo(generate id automatically)
    val id: Int,
    val countryselected: String,
    val countrycode: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    var phone: String,
    var role: Int,
    var typerecharge:String,
    var date:String,
    var subtotal: String,
    var description:String
)
