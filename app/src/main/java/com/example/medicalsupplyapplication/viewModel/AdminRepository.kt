package com.example.medicalsupplyapplication.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.database.model.Admin
import com.google.firebase.Timestamp

class AdminRepository {

    private var admin: MutableLiveData<Admin> = MutableLiveData()
    private var adminList: MutableLiveData<MutableList<Admin>> = MutableLiveData()

    fun getAdminListFirestore(): MutableLiveData<MutableList<Admin>> {
        val admins: MutableList<Admin> = mutableListOf()

        Database.db.collection("Admin")
            .get()
            .addOnSuccessListener { adminDatabase ->
                for (admin in adminDatabase) {
                    val adminID: String = admin.data["AdminID"].toString()
                    val adminName: String = admin.data["AdminName"].toString()
                    val email: String = admin.data["Email"].toString()
                    val phone: String = admin.data["Phone"].toString()
                    val password: String = admin.data["Password"].toString()
                    val confirmPass: String = admin.data["ConfirmPass"].toString()
                    val lastUpdated: Timestamp? = admin.getTimestamp("LastUpdated")

                    val admin = Admin(
                        adminID,
                        adminName,
                        email,
                        phone,
                        password,
                        confirmPass,
                        lastUpdated
                    )
                    admins.add(admin)
                }
                adminList.postValue(admins)
            }
        return adminList
    }

    fun getSingleAdminFirestore(adminID: String): MutableLiveData<Admin> {

        Database.db.collection("Admin")
            .get()
            .addOnSuccessListener { adminDatabase ->
                for (admin in adminDatabase) {
                    if (adminID == admin.data["AdminID"].toString()) {
                        val adminID: String = admin.data["AdminID"].toString()
                        val adminName: String = admin.data["AdminName"].toString()
                        val email: String = admin.data["Email"].toString()
                        val phone: String = admin.data["Phone"].toString()
                        val password: String = admin.data["Password"].toString()
                        val confirmPass: String = admin.data["ConfirmPass"].toString()
                        val lastUpdated: Timestamp? = admin.getTimestamp("LastUpdated")

                        val admin = Admin(
                            adminID,
                            adminName,
                            email,
                            phone,
                            password,
                            confirmPass,
                            lastUpdated
                        )
                        this.admin.postValue(admin)
                    }
                }
            }
        return admin
    }

    fun updateSingleAdminFirestore(admin: Admin) {
        val adminData = mapOf(
            "AdminID" to admin.adminID,
            "AdminName" to admin.adminName,
            "Email" to admin.email,
            "Phone" to admin.phone,
            "Password" to admin.password,
            "ConfirmPass" to admin.confirmPass,
            "LastUpdated" to admin.lastUpdated
        )

        Database.db.collection("Admin").document(admin.adminID)
            .update(adminData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error update data", it)
            }
    }

    fun addSingleAdminFirestore(admin: Admin) {
        val adminData = mapOf(
            "AdminID" to admin.adminID,
            "AdminName" to admin.adminName,
            "Email" to admin.email,
            "Phone" to admin.phone,
            "Password" to admin.password,
            "ConfirmPass" to admin.confirmPass,
            "LastUpdated" to admin.lastUpdated
        )

        Database.db.collection("Admin").document(admin.adminID)
            .set(adminData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error add new data", it)
            }
    }

    fun getAdminList(): MutableLiveData<MutableList<Admin>> {
        return adminList
    }

    fun getAdmin(): MutableLiveData<Admin> {
        return admin
    }
}