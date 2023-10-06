package com.example.medicalsupplyapplication.admin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.customer.HomePageActivity
import com.example.medicalsupplyapplication.databinding.ActivityManageBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization

class ManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginID = intent.getStringExtra("getID")

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_dashboard -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("getID", loginID)
                    startActivity(intent)
                }

                R.id.ic_menu -> {
                    val intent = Intent(this, ManageActivity::class.java)
                    intent.putExtra("getID", loginID)
                    startActivity(intent)
                }

                R.id.ic_logout -> {
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        val manageList = binding.manageList
        val names =
            arrayOf(" Manage Product", " Manage Admin", " Manage Delivery")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, names
        )

        manageList.adapter = arrayAdapter

        manageList.setOnItemClickListener { adapterView, view, position, l ->
            val synchronization = Synchronization()

            if (position == 0) {
                if(synchronization.isNetworkAvailable(this)){
                    goOther(ProductListActivity())
                }else{
                    Toast.makeText(this, "Please ensure internet is available before manage product.", Toast.LENGTH_SHORT).show()
                }
            } else if (position == 1) {
                if(synchronization.isNetworkAvailable(this)){
                    goOther(AdminListActivity())
                }else{
                    Toast.makeText(this, "Please ensure internet is available before manage admin.", Toast.LENGTH_SHORT).show()
                }
            } else if (position == 2) {
                if(synchronization.isNetworkAvailable(this)){
                    goOther(DeliveryControlActivity())
                }else{
                    Toast.makeText(this, "Please ensure internet is available before manage delivery.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goOther(activity: Activity) {
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this, activity::class.java)
        intent.putExtra("getID", loginID)
        startActivity(intent)
    }
}