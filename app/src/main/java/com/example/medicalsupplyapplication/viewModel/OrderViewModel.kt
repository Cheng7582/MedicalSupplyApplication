package com.example.medicalsupplyapplication.viewModel

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.database.model.Order

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private var _order: MediatorLiveData<Order> = MediatorLiveData()
    var order: MediatorLiveData<Order>
        get() = _order
        set(value){
            _order = value
        }

    private var _orderList: MediatorLiveData<MutableList<Order>> = MediatorLiveData()
    var orderList: MediatorLiveData<MutableList<Order>>
        get() = _orderList
        set(value) {
            _orderList = value
        }

    constructor() : this(OrderRepository()) {
    }

    init {
        _orderList.addSource(orderRepository.getOrderList()){ newOrderList->
            _orderList.value = newOrderList
        }

        _order.addSource(orderRepository.getOrder()){ newOrder->
            _order.value = newOrder
        }
    }

    fun initOnlineOrderList() {
        orderRepository.getOrderListFirestore()
    }

    fun initOnlineOrder(orderID: String) {
        orderRepository.getSingleOrderFirestore(orderID)
    }

    fun addSingleOrderOnline(order: Order){
        orderRepository.addSingleOrderFirestore(order)
    }

    fun updateSingleOrderOnline(order: Order){
        orderRepository.updateSingleOrderFirestore(order)
    }

    suspend fun initOfflineOrderList(context: Context) {
        orderRepository.getOrderListRoom(context)
    }

}