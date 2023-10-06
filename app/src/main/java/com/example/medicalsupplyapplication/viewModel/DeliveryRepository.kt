package com.example.medicalsupplyapplication.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.database.model.Delivery
import com.google.firebase.Timestamp

class DeliveryRepository {
    private var delivery: MutableLiveData<Delivery> = MutableLiveData()
    private var deliveryList: MutableLiveData<MutableList<Delivery>> = MutableLiveData()

    fun getDeliveryListFirestore(): MutableLiveData<MutableList<Delivery>> {
        val deliveries: MutableList<Delivery> = mutableListOf()

        Database.db.collection("Delivery")
            .get()
            .addOnSuccessListener { deliveryDatabase ->
                for (delivery in deliveryDatabase) {
                    val deliveryID: String = delivery.data["DeliveryID"].toString()
                    val custID: String = delivery.data["CustID"].toString()
                    val payID: String = delivery.data["PayID"].toString()
                    val status: String = delivery.data["Status"].toString()
                    val lastUpdated: Timestamp? = delivery.getTimestamp("LastUpdated")

                    val delivery = Delivery(
                        deliveryID,
                        custID,
                        payID,
                        status,
                        lastUpdated
                    )
                    deliveries.add(delivery)
                }
                deliveryList.postValue(deliveries)
            }
        return deliveryList
    }

    fun getSingleDeliveryFirestore(deliveryID: String): MutableLiveData<Delivery> {

        Database.db.collection("Delivery")
            .get()
            .addOnSuccessListener { deliveryDatabase ->
                for (delivery in deliveryDatabase) {
                    if (deliveryID == delivery.data["DeliveryID"].toString()) {
                        val deliveryID: String = delivery.data["DeliveryID"].toString()
                        val custID: String = delivery.data["CustID"].toString()
                        val payID: String = delivery.data["PayID"].toString()
                        val status: String = delivery.data["Status"].toString()
                        val lastUpdated: Timestamp? = delivery.getTimestamp("LastUpdated")

                        val delivery = Delivery(
                            deliveryID,
                            custID,
                            payID,
                            status,
                            lastUpdated
                        )
                        this.delivery.postValue(delivery)
                    }
                }
            }
        return delivery
    }

    fun updateSingleDeliveryFirestore(delivery: Delivery) {
        val deliveryData = mapOf(
            "deliveryID" to delivery.deliveryID,
            "custID" to delivery.custID,
            "payID" to delivery.payID,
            "status" to delivery.status,
            "lastUpdated" to delivery.lastUpdated
        )

        Database.db.collection("Delivery").document(delivery.deliveryID)
            .update(deliveryData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error update data", it)
            }
    }

    fun addSingleDeliveryFirestore(delivery: Delivery) {
        val deliveryData = mapOf(
            "deliveryID" to delivery.deliveryID,
            "custID" to delivery.custID,
            "payID" to delivery.payID,
            "status" to delivery.status,
            "lastUpdated" to delivery.lastUpdated
        )

        Database.db.collection("Delivery").document(delivery.deliveryID)
            .set(deliveryData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error add new data", it)
            }
    }

    fun getDeliveryList(): MutableLiveData<MutableList<Delivery>> {
        return deliveryList
    }

    fun getDelivery(): MutableLiveData<Delivery> {
        return delivery
    }

}