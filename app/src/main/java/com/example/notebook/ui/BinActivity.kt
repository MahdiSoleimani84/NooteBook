package com.example.notebook.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebook.R
import com.example.notebook.adapter.recycler.NotesAdapter
import com.example.notebook.adapter.recycler.RecycleHolder
import com.example.notebook.data.local.db.DBHelper
import com.example.notebook.data.local.db.dao.NotesDao
import com.example.notebook.databinding.ActivityBinBinding

class BinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()

    }


    private fun initRecycler() {

        val dao = NotesDao(DBHelper(this))
        val adapter = RecycleHolder(this, dao)

        binding.RecyclerNotes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.RecyclerNotes.adapter = adapter

    }

}