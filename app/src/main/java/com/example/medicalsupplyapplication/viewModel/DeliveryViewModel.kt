package com.example.medicalsupplyapplication.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.database.model.Delivery

class DeliveryViewModel(private val deliveryRepository: DeliveryRepository) : ViewModel() {

    private var _delivery: MediatorLiveData<Delivery> = MediatorLiveData()
    var delivery: MediatorLiveData<Delivery>
        get() = _delivery
        set(value){
            _delivery = value
        }

    private var _deliveryList: MediatorLiveData<MutableList<Delivery>> = MediatorLiveData()
    var deliveryList: MediatorLiveData<MutableList<Delivery>>
        get() = _deliveryList
        set(value) {
            _deliveryList = value
        }

    constructor() : this(DeliveryRepository()) {
    }
    init {
        _deliveryList.addSource(deliveryRepository.getDeliveryList()){ newDeliveryList->
            _deliveryList.value = newDeliveryList
        }

        _delivery.addSource(deliveryRepository.getDelivery()){ newDelivery->
            _delivery.value = newDelivery
        }
    }


    fun initOnlineDeliveryList() {
        deliveryRepository.getDeliveryListFirestore()
    }

    fun initOnlineDelivery(deliveryID: String) {
        deliveryRepository.getSingleDeliveryFirestore(deliveryID)
    }

    fun addSingleDeliveryOnline(delivery: Delivery){
        deliveryRepository.addSingleDeliveryFirestore(delivery)
    }

    fun updateSingleDeliveryOnline(delivery: Delivery){
        deliveryRepository.updateSingleDeliveryFirestore(delivery)
    }

}