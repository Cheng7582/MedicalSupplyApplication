package com.example.medicalsupplyapplication.database.model

import com.google.firebase.Timestamp

data class Admin(
    var adminID: String,
    var adminName: String,
    var email: String,
    var phone: String,
    var password: String,
    var confirmPass: String,
    var lastUpdated: Timestamp?,
    var updateFlag: Boolean
) {
    constructor(
        adminID: String,
        adminName: String,
        email: String,
        phone: String,
        password: String,
        confirmPass: String,
        lastUpdated: Timestamp?
    ) : this(
        adminID,
        adminName,
        email,
        phone,
        password,
        confirmPass,
        lastUpdated,
        false
    )
}