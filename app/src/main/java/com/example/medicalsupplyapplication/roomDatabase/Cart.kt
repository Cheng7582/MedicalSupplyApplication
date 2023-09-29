package com.example.medicalsupplyapplication.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart", primaryKeys = ["cartID", "prodID"])
data class Cart (
    @ColumnInfo(name = "cartID")
    val cartID: String,

    @ColumnInfo(name = "custID")
    var custID: String,

    @ColumnInfo(name = "prodID")
    var prodID: String,

    @ColumnInfo(name = "prodName")
    var prodName: String,

    @ColumnInfo(name = "prodPrice")
    var prodPrice: String,

    @ColumnInfo(name = "qty")
    var qty: String,

    @ColumnInfo(name = "idNum")
    var idNum: String,

    @ColumnInfo(name = "lastUpdated")
    var lastUpdated: String
)