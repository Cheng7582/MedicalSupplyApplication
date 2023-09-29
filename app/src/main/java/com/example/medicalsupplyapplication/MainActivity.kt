package com.example.medicalsupplyapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.medicalsupplyapplication.customer.HomePageActivity
import com.example.medicalsupplyapplication.databinding.ActivityMainBinding
import com.example.medicalsupplyapplication.roomDatabase.Admin
import com.example.medicalsupplyapplication.roomDatabase.MedicalRoomDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Database.getAllDataFromFirestore()

        val database = MedicalRoomDatabase.getInstance(this@MainActivity)

        //-----------------Testing---------------------------
        val admin = Admin(
            adminID = "your_admin_id",
            adminName = "John Doe",
            password = "your_password",
            confirmPass = "your_confirm_password",
            email = "johndoe@example.com",
            phone = "123-456-7890",
            lastUpdated = "2023-09-29"
        )

        lifecycleScope.launch {
            // Your suspend function call
            database.medicalRoomDao.insertAdmin(admin)
        }
        //-----------------Testing---------------------------


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("getID","")
        startActivity(intent)
    }

}