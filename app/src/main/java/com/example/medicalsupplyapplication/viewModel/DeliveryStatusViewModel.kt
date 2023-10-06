package com.example.medicalsupplyapplication.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.database.model.DeliveryStatus

class DeliveryStatusViewModel(private val deliveryStatusRepository: DeliveryStatusRepository) : ViewModel() {

    private var _deliveryStatus: MediatorLiveData<DeliveryStatus> = MediatorLiveData()
    var deliveryStatus: MediatorLiveData<DeliveryStatus>
        get() = _deliveryStatus
        set(value){
            _deliveryStatus = value
        }

    private var _deliveryStatusList: MediatorLiveData<MutableList<DeliveryStatus>> = MediatorLiveData()
    var deliveryStatusList: MediatorLiveData<MutableList<DeliveryStatus>>
        get() = _deliveryStatusList
        set(value) {
            _deliveryStatusList = value
        }

    constructor() : this(DeliveryStatusRepository()) {
    }

    init {
        _deliveryStatusList.addSource(deliveryStatusRepository.getDeliveryStatusList()){ newDeliveryStatusList->
            _deliveryStatusList.value = newDeliveryStatusList
        }

        _deliveryStatus.addSource(deliveryStatusRepository.getDeliveryStatus()){ newDeliveryStatus->
            _deliveryStatus.value = newDeliveryStatus
        }
    }

    fun initOnlineDeliveryStatusList() {
        deliveryStatusRepository.getDeliveryStatusListFirestore()
    }

    fun initOnlineDeliveryStatus(deliveryStatusID: String) {
        deliveryStatusRepository.getSingleDeliveryStatusFirestore(deliveryStatusID)
    }

    fun addSingleDeliveryStatusOnline(deliveryStatus: DeliveryStatus){
        deliveryStatusRepository.addSingleDeliveryStatusFirestore(deliveryStatus)
    }

    fun updateSingleDeliveryStatusOnline(deliveryStatus: DeliveryStatus){
        deliveryStatusRepository.updateSingleDeliveryStatusFirestore(deliveryStatus)
    }
}