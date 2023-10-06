package com.example.medicalsupplyapplication.database.model

import com.google.firebase.Timestamp

data class Delivery(

    var deliveryID: String,
    var custID: String,
    var payID: String,
    var status: String,
    var lastUpdated: Timestamp?,
    var updateFlag: Boolean
) {
    constructor(
        deliveryID: String,
        custID: String,
        payID: String,
        status: String,
        lastUpdated: Timestamp?
    ) : this(
        deliveryID,
        custID,
        payID,
        status,
        lastUpdated,
        false
    )
}