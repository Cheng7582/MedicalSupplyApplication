package com.example.medicalsupplyapplication.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.database.model.Admin

class AdminViewModel(private val AdminRepository: AdminRepository) : ViewModel() {

    private var _admin: MediatorLiveData<Admin> = MediatorLiveData()
    var admin: MediatorLiveData<Admin>
        get() = _admin
        set(value){
            _admin = value
        }

    private var _adminList: MediatorLiveData<MutableList<Admin>> = MediatorLiveData()
    var adminList: MediatorLiveData<MutableList<Admin>>
        get() = _adminList
        set(value) {
            _adminList = value
        }

    constructor() : this(AdminRepository()) {
    }

    init {
        _adminList.addSource(AdminRepository.getAdminList()){ newAdminList->
            _adminList.value = newAdminList
        }

        _admin.addSource(AdminRepository.getAdmin()){ newAdmin->
            _admin.value = newAdmin
        }
    }

    fun initOnlineAdminList() {
        AdminRepository.getAdminListFirestore()
    }

    fun initOnlineAdmin(adminID: String) {
        AdminRepository.getSingleAdminFirestore(adminID)
    }

    fun addSingleAdminOnline(admin: Admin){
        AdminRepository.addSingleAdminFirestore(admin)
    }

    fun updateSingleAdminOnline(admin: Admin){
        AdminRepository.updateSingleAdminFirestore(admin)
    }
}