package com.example.medicalsupplyapplication.database.model

import com.google.firebase.Timestamp

data class Product(

    var productID: String,
    var productName: String,
    var price: Int,
    var stock: Int,
    var desc: String,
    var brand: String,
    var category: String,
    var lastUpdated: Timestamp?,
    var updateFlag: Boolean
) {
    constructor(
        productID: String,
        productName: String,
        price: Int,
        stock: Int,
        desc: String,
        brand: String,
        category: String,
        lastUpdated: Timestamp?
    ) : this(
        productID,
        productName,
        price,
        stock,
        desc,
        brand,
        category,
        lastUpdated,
        false
    )

}

