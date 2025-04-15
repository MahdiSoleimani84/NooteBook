package com.example.notebook.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notebook.data.local.db.DBHelper
import com.example.notebook.data.local.db.dao.NotesDao
import com.example.notebook.data.model.NotesModel
import com.example.notebook.databinding.ActivityAddNotesBinding


class AddNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val dao = NotesDao(DBHelper(this))

        val type = intent.getBooleanExtra("newNotes", false)
        val id = intent.getIntExtra("id", 0)

        if (type){

            binding.edtDescriptionNotes.hint = "توضیحات یادداشت خود را وارد کنید"

        }else{
            val data = dao.getNotesById(id)
            binding.edtTitleNotes.setText(data.title)
            binding.edtDescriptionNotes.setText(data.description)
        }

        binding.saveNote.setOnClickListener {
            val title = binding.edtTitleNotes.text.toString()
            val description = binding.edtDescriptionNotes.text.toString()



            if (title.isNotEmpty()) {
//ارور احتمالی از اینجا
                val note = NotesModel(0, title, description, DBHelper.FALSE_STATE)

                val result =
                if (type) dao.saveNotes(note)
                else dao.editNotes(id, note)

                if (result) {
                    toast("با موفقیت ثبت شد")

                    finish()
                }
                else {
                    toast("error")
                }

            } else {
                toast("لطفا عنوان را وارد کنید")
            }
        }


        binding.cancelNote.setOnClickListener { finish() }

    }



    private fun toast(text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


}