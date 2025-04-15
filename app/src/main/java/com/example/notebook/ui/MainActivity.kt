package com.example.notebook.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebook.adapter.recycler.NotesAdapter
import com.example.notebook.data.local.db.DBHelper
import com.example.notebook.data.local.db.dao.NotesDao
import com.example.notebook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dao: NotesDao
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initRecycler()


        binding.imageAddNote.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("newNotes",true)
            startActivity(intent)


        }

    }

    override fun onStart() {
        super.onStart()
        val dataShow = dao.getNotesFromRecycler(DBHelper.FALSE_STATE)
        adapter.changData(dataShow)

    }


    private fun initRecycler() {

        dao = NotesDao(DBHelper(this))
        val finalData = dao.getNotesFromRecycler(DBHelper.FALSE_STATE)
        adapter = NotesAdapter(this, finalData, dao)

        binding.RecyclerNotes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.RecyclerNotes.adapter = adapter

    }

}


