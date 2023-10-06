package com.example.medicalsupplyapplication.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.database.model.DeliveryStatus
import com.google.firebase.Timestamp

class DeliveryStatusRepository {
    private var deliveryStatus: MutableLiveData<DeliveryStatus> = MutableLiveData()
    private var deliveryStatusList: MutableLiveData<MutableList<DeliveryStatus>> = MutableLiveData()

    fun getDeliveryStatusListFirestore(): MutableLiveData<MutableList<DeliveryStatus>> {
        val deliveriesStatus: MutableList<DeliveryStatus> = mutableListOf()

        Database.db.collection("DeliveryStatus")
            .get()
            .addOnSuccessListener { deliveryStatusDatabase ->
                for (deliveryStatus in deliveryStatusDatabase) {
                    val deliveryStatusID: String = deliveryStatus.data["DsID"].toString()
                    val deliveryID: String = deliveryStatus.data["DeliveryID"].toString()
                    val date: Timestamp? = deliveryStatus.getTimestamp("Date")
                    val desc: String = deliveryStatus.data["Desc"].toString()
                    val lastUpdated: Timestamp? = deliveryStatus.getTimestamp("LastUpdated")

                    val deliveryStatus = DeliveryStatus(
                        deliveryStatusID,
                        deliveryID,
                        date,
                        desc,
                        lastUpdated
                    )
                    deliveriesStatus.add(deliveryStatus)
                }
                deliveryStatusList.postValue(deliveriesStatus)
            }
        return deliveryStatusList
    }

    fun getSingleDeliveryStatusFirestore(deliveryStatusID: String): MutableLiveData<DeliveryStatus> {

        Database.db.collection("Product")
            .get()
            .addOnSuccessListener { deliveryStatusDatabase ->
                for (deliveryStatus in deliveryStatusDatabase) {
                    if (deliveryStatusID == deliveryStatus.data["DsID"].toString()) {
                        val deliveryStatusID: String = deliveryStatus.data["DsID"].toString()
                        val deliveryID: String = deliveryStatus.data["DeliveryID"].toString()
                        val date: Timestamp? = deliveryStatus.getTimestamp("Date")
                        val desc: String = deliveryStatus.data["Desc"].toString()
                        val lastUpdated: Timestamp? = deliveryStatus.getTimestamp("LastUpdated")

                        val deliveryStatus = DeliveryStatus(
                            deliveryStatusID,
                            deliveryID,
                            date,
                            desc,
                            lastUpdated
                        )
                        this.deliveryStatus.postValue(deliveryStatus)
                    }
                }
            }
        return deliveryStatus
    }

    fun updateSingleDeliveryStatusFirestore(deliveryStatus: DeliveryStatus) {
        val deliveryStatusData = mapOf(
            "DsID" to deliveryStatus.deliveryStatusID,
            "DeliveryID" to deliveryStatus.deliveryID,
            "Date" to deliveryStatus.date,
            "Desc" to deliveryStatus.desc,
            "LastUpdated" to deliveryStatus.lastUpdated
        )

        Database.db.collection("Delivery").document(deliveryStatus.deliveryStatusID)
            .update(deliveryStatusData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error update data", it)
            }
    }

    fun addSingleDeliveryStatusFirestore(deliveryStatus: DeliveryStatus) {
        val deliveryStatusData = mapOf(
            "DsID" to deliveryStatus.deliveryStatusID,
            "DeliveryID" to deliveryStatus.deliveryID,
            "Date" to deliveryStatus.date,
            "Desc" to deliveryStatus.desc,
            "LastUpdated" to deliveryStatus.lastUpdated
        )

        Database.db.collection("Delivery").document(deliveryStatus.deliveryStatusID)
            .set(deliveryStatusData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error add new data", it)
            }
    }

    fun getDeliveryStatusList(): MutableLiveData<MutableList<DeliveryStatus>> {
        return deliveryStatusList
    }

    fun getDeliveryStatus(): MutableLiveData<DeliveryStatus> {
        return deliveryStatus
    }
}