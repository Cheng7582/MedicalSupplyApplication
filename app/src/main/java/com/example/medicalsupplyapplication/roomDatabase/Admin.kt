package com.example.medicalsupplyapplication.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.reflect.Constructor

@Entity(tableName = "admin")
data class Admin(
    @PrimaryKey(autoGenerate = false)
    val adminID: String,

    @ColumnInfo(name = "adminName")
    var adminName: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "confirmPass")
    var confirmPass: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "phone")
    var phone: String,

    @ColumnInfo(name = "lastUpdated")
    var lastUpdated: String


)
