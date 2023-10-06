package com.example.medicalsupplyapplication.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order")
data class Order (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "orderID")
    var orderID: String,

    @ColumnInfo(name = "prodID")
    var prodID: String,

    @ColumnInfo(name = "custID")
    var custID: String,

    @ColumnInfo(name = "paymentID")
    var paymentID: String,

    @ColumnInfo(name = "payTime")
    var payTime: Long?,

    @ColumnInfo(name = "prodName")
    var prodName: String,

    @ColumnInfo(name = "prodPrice")
    var prodPrice: Int,

    @ColumnInfo(name = "prodQty")
    var prodQty: Int,

    @ColumnInfo(name = "lastUpdated")
    var lastUpdated: Long?,

    @ColumnInfo(name = "updateFlag")
    var updateFlag: Boolean

    ){
    // Empty constructor for Room
    constructor() : this("","","","",0,"",0,0,0,false)
}