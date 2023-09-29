package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.databinding.ActivityAdminListBinding

class AdminListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminListBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<AdminListAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getLoginID = intent.getStringExtra("getID")

        layoutManager = LinearLayoutManager(this)

        binding.recycleViewAdmin.layoutManager = layoutManager

        AdminListAdapter.setFragment(getLoginID.toString(),this)

        adapter = AdminListAdapter()
        binding.recycleViewAdmin.adapter = adapter

        binding.goAddAdminBtn.setOnClickListener {
            val intent = Intent(this,AddAdminActivity::class.java)
            intent.putExtra("getID",getLoginID)
            startActivity(intent)
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,ManageActivity::class.java)
            intent.putExtra("getID",getLoginID)
            startActivity(intent)
        }

    }

    fun toDetail(index: Int){
        val intent = Intent(this, AdminDetailsActivity::class.java)
        intent.putExtra("getIndex",index)
        startActivity(intent)
    }
}