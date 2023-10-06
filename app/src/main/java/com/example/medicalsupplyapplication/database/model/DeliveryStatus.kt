package com.example.medicalsupplyapplication.database.model

import com.google.firebase.Timestamp

data class DeliveryStatus(
    var deliveryStatusID: String,
    var deliveryID: String,
    var date: Timestamp?,
    var desc: String,
    var lastUpdated: Timestamp?,
    var updateFlag: Boolean

) {
    constructor(
        deliveryStatusID: String,
        deliveryID: String,
        date: Timestamp?,
        desc: String,
        lastUpdated: Timestamp?
    ) : this(
        deliveryStatusID,
        deliveryID,
        date,
        desc,
        lastUpdated,
        false
    )
}
