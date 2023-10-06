package com.example.medicalsupplyapplication.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.database.model.Customer

class CustomerViewModel(private val customerRepository: CustomerRepository) : ViewModel() {
    private var _customer: MediatorLiveData<Customer> = MediatorLiveData()
    var customer: MediatorLiveData<Customer>
        get() = _customer
        set(value){
            _customer = value
        }

    private var _customerList: MediatorLiveData<MutableList<Customer>> = MediatorLiveData()
    var customerList: MediatorLiveData<MutableList<Customer>>
        get() = _customerList
        set(value) {
            _customerList = value
        }

    constructor() : this(CustomerRepository()) {
    }

    init {
        _customerList.addSource(customerRepository.getCustomerList()){ newCustList->
            _customerList.value = newCustList
        }

        _customer.addSource(customerRepository.getCustomer()){ newCust->
            _customer.value = newCust
        }
    }

    fun initOnlineCustomerList() {
        customerRepository.getCustomerListFirestore()
    }

    fun initOnlineCustomer(customerID: String) {
        customerRepository.getSingleCustomerFirestore(customerID)
    }

    fun addSingleCustomerOnline(customer: Customer){
        customerRepository.addSingleCustomerFirestore(customer)
    }

    fun updateSingleCustomerOnline(customer: Customer){
        customerRepository.updateSingleCustomerFirestore(customer)
    }
}