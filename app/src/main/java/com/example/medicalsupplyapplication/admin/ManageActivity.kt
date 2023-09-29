package com.example.medicalsupplyapplication.admin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.customer.HomePageActivity
import com.example.medicalsupplyapplication.databinding.ActivityManageBinding

class ManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prod = ProductListActivity()
        val adm = AdminListActivity()
        val dls = DeliveryControlActivity()

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
            if (position == 0) {
                goOther(prod)
            } else if (position == 1) {
                goOther(adm)
            } else if (position == 2) {
                goOther((dls))
            }
        }

        //See here still need or not
/*        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.ic_admin_list -> {
                    val loginID = intent.getStringExtra("getID")
                    val intent = Intent(this, AdminListActivity::class.java)
                    intent.putExtra("getID", loginID)
                    startActivity(intent)
                }

                R.id.ic_product_list -> {
                    val loginID = intent.getStringExtra("getID")
                    val intent = Intent(this, ProductListActivity::class.java)
                    intent.putExtra("getID", loginID)
                    startActivity(intent)
                }
            }
            true
        }*/
    }

    private fun goOther(activity: Activity) {
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this, activity::class.java)
        intent.putExtra("getID", loginID)
        startActivity(intent)
    }
}