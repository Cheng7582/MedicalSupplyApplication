package com.example.medicalsupplyapplication.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.database.model.Customer
import com.google.firebase.Timestamp

class CustomerRepository {
    private var customer: MutableLiveData<Customer> = MutableLiveData()
    private var customerList: MutableLiveData<MutableList<Customer>> = MutableLiveData()

    fun getCustomerListFirestore(): MutableLiveData<MutableList<Customer>> {
        val customers: MutableList<Customer> = mutableListOf()

        Database.db.collection("Customer")
            .get()
            .addOnSuccessListener { custDatabase ->
                for (customer in custDatabase) {
                    val custID: String = customer.data["CustID"].toString()
                    val username: String = customer.data["Username"].toString()
                    val email: String = customer.data["Email"].toString()
                    val phone: String = customer.data["Phone"].toString()
                    val password: String = customer.data["Password"].toString()
                    val confirmPass: String = customer.data["ConfirmPass"].toString()
                    val address: String = customer.data["Address"].toString()
                    val registerDate: Timestamp? = customer.getTimestamp("RegisterDate")
                    val status: String = customer.data["Status"].toString()
                    val lastUpdated: Timestamp? = customer.getTimestamp("LastUpdated")

                    val customer = Customer(
                        custID,
                        username,
                        email,
                        phone,
                        password,
                        confirmPass,
                        address,
                        registerDate,
                        status,
                        lastUpdated
                    )
                    customers.add(customer)
                }
                customerList.postValue(customers)
            }
        return customerList
    }

    fun getSingleCustomerFirestore(customerID: String): MutableLiveData<Customer> {

        Database.db.collection("Customer")
            .get()
            .addOnSuccessListener { custDatabase ->
                for (customer in custDatabase) {
                    if (customerID == customer.data["CustID"].toString()) {
                        val custID: String = customer.data["CustID"].toString()
                        val username: String = customer.data["Username"].toString()
                        val email: String = customer.data["Email"].toString()
                        val phone: String = customer.data["Phone"].toString()
                        val password: String = customer.data["Password"].toString()
                        val confirmPass: String = customer.data["ConfirmPass"].toString()
                        val address: String = customer.data["Address"].toString()
                        val registerDate: Timestamp? = customer.getTimestamp("RegisterDate")
                        val status: String = customer.data["Status"].toString()
                        val lastUpdated: Timestamp? = customer.getTimestamp("LastUpdated")

                        val customer = Customer(
                            custID,
                            username,
                            email,
                            phone,
                            password,
                            confirmPass,
                            address,
                            registerDate,
                            status,
                            lastUpdated
                        )
                        this.customer.postValue(customer)
                    }
                }
            }
        return customer
    }

    fun updateSingleCustomerFirestore(customer: Customer) {
        val customerData = mapOf(
            "CustID" to customer.custID,
            "Username" to customer.username,
            "Email" to customer.email,
            "Phone" to customer.phone,
            "Password" to customer.password,
            "ConfirmPass" to customer.confirmPass,
            "Address" to customer.address,
            "RegisterDate" to customer.registerDate,
            "Status" to customer.status,
            "LastUpdated" to customer.lastUpdated
        )

        Database.db.collection("Customer").document(customer.custID)
            .update(customerData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error update data", it)
            }
    }

    fun addSingleCustomerFirestore(customer: Customer) {
        val customerData = mapOf(
            "CustID" to customer.custID,
            "Username" to customer.username,
            "Email" to customer.email,
            "Phone" to customer.phone,
            "Password" to customer.password,
            "ConfirmPass" to customer.confirmPass,
            "Address" to customer.address,
            "RegisterDate" to customer.registerDate,
            "Status" to customer.status,
            "LastUpdated" to customer.lastUpdated
        )

        Database.db.collection("Customer").document(customer.custID)
            .set(customerData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error add new data", it)
            }
    }

    fun getCustomerList(): MutableLiveData<MutableList<Customer>> {
        return customerList
    }

    fun getCustomer(): MutableLiveData<Customer> {
        return customer
    }
}