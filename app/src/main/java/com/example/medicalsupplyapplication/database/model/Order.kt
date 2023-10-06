package com.example.medicalsupplyapplication.database.model

import com.google.firebase.Timestamp

data class Order(
    var orderID: String,
    var prodID: String,
    var custID: String,
    var paymentID: String,
    var payTime: Timestamp?,
    var prodName: String,
    var prodPrice: Int,
    var prodQty: Int,
    var lastUpdated: Timestamp?,
    var updateFlag: Boolean
) {
    constructor(
        orderID: String,
        prodID: String,
        custID: String,
        paymentID: String,
        payTime: Timestamp?,
        prodName: String,
        prodPrice: Int,
        prodQty: Int,
        lastUpdated: Timestamp?
    ) : this(
        orderID,
        prodID,
        custID,
        paymentID,
        payTime,
        prodName,
        prodPrice,
        prodQty,
        lastUpdated,
        false
    )

}