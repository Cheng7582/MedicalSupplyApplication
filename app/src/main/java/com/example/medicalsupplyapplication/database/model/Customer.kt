package com.example.medicalsupplyapplication.database.model

import com.google.firebase.Timestamp

data class Customer(
    var custID: String,
    var username: String,
    var email: String,
    var phone: String,
    var password: String,
    var confirmPass: String,
    var address: String,
    var registerDate: Timestamp?,
    var status: String,
    var lastUpdated: Timestamp?,
    var updateFlag: Boolean

) {
    constructor(
        custID: String,
        username: String,
        email: String,
        phone: String,
        password: String,
        confirmPass: String,
        address: String,
        registerDate: Timestamp?,
        status: String,
        lastUpdated: Timestamp?
    ) : this(
        custID,
        username,
        email,
        phone,
        password,
        confirmPass,
        address,
        registerDate,
        status,
        lastUpdated,
        false
    )
}