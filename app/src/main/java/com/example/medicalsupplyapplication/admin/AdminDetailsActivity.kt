package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityAdminDetailsBinding

class AdminDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminPosition = intent.getIntExtra("getIndex",0)

        binding.getAdminID.text = Database.admins[adminPosition].getID()
        binding.getAdminName.text = Database.admins[adminPosition].getName()
        binding.getAdminEmail.text = Database.admins[adminPosition].getEmail()
        binding.getAdminPhone.text = Database.admins[adminPosition].getPhone()

        binding.editAdminBtn.setOnClickListener {
            val intent = Intent(this,UpdateAdminActivity::class.java)
            intent.putExtra("getPosition",adminPosition)
            startActivity(intent)
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,AdminListActivity::class.java)
            intent.putExtra("getID",adminPosition)
            startActivity(intent)
        }
    }
}