package com.example.medicalsupplyapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicalsupplyapplication.customer.HomePageActivity
import com.example.medicalsupplyapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Database.getAllDataFromFirestore()

        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("getID","")
        startActivity(intent)
    }



}