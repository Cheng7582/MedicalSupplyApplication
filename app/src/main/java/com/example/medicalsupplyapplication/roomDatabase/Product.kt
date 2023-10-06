package com.example.medicalsupplyapplication.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "productID")
    var productID: String,

    @ColumnInfo(name = "productName")
    var productName: String,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "stock")
    var stock: Int,

    @ColumnInfo(name = "desc")
    var desc: String,

    @ColumnInfo(name = "brand")
    var brand: String,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "LastUpdated")
    var lastUpdated: Long?,

    @ColumnInfo(name = "updateFlag")
    var updateFlag: Boolean
) {

    // Empty constructor for Room
    constructor() : this("", "", 0, 0, "", "", "", null, false)
}