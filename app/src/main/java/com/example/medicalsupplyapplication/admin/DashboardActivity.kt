package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.customer.HomePageActivity
import com.example.medicalsupplyapplication.databinding.ActivityDashboardBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginID = intent.getStringExtra("getID")


        //If network available sync record to room for offline purpose
        val synchronization = Synchronization()
        if(synchronization.isNetworkAvailable(this)){
            val coroutineScope = lifecycleScope
            lifecycleScope.launch {
                synchronization.syncProductDataToRoom(applicationContext, coroutineScope)
                synchronization.syncOrderDataToRoom(applicationContext, coroutineScope)
            }
        }

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

        binding.monthReport.setOnClickListener {
            val intent = Intent(this, MonthlyReportActivity::class.java)
            intent.putExtra("getID", loginID)
            startActivity(intent)
        }

        binding.stockReport.setOnClickListener {
            val intent = Intent(this, StockReportActivity::class.java)
            intent.putExtra("getID", loginID)
            startActivity(intent)
        }

    }
}