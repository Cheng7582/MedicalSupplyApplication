package com.example.medicalsupplyapplication.database.model

import com.google.firebase.Timestamp


data class Cart(
    var cartID: String,
    var prodID: String,
    var custID: String,
    var prodName: String,
    var prodPrice: Int,
    var prodQty: Int,
    var idNum: Int,
    var lastUpdated: Timestamp?,
    var updateFlag: Boolean
) {
    constructor(
        cartID: String,
        prodID: String,
        custID: String,
        prodName: String,
        prodPrice: Int,
        prodQty: Int,
        idNum: Int,
        lastUpdated: Timestamp?
    ) : this(
        cartID,
        prodID,
        custID,
        prodName,
        prodPrice,
        prodQty,
        idNum,
        lastUpdated,
        false
    )
}