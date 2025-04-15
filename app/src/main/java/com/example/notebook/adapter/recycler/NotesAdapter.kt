package com.example.notebook.adapter.recycler

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.R
import com.example.notebook.data.local.db.DBHelper
import com.example.notebook.data.local.db.dao.NotesDao
import com.example.notebook.data.model.RecyclerNotesModel
import com.example.notebook.databinding.ListItemNotesBinding
import com.example.notebook.ui.AddNotesActivity

class NotesAdapter
    (
    private val context: Activity,
    private var allData: ArrayList<RecyclerNotesModel>,
    private val dao: NotesDao
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotesViewHolder(
            ListItemNotesBinding.inflate(context.layoutInflater, parent, false)

        )

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) =
        holder.setData(allData[position])


    override fun getItemCount() = allData.size





    inner class NotesViewHolder
        (private val view: ListItemNotesBinding)
        : RecyclerView.ViewHolder(view.root){

        fun setData(data: RecyclerNotesModel) {

            view.txtTitleNotes.text = data.title

            view.imgDeleteNotes.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
                    .setTitle("حدف یادداشت")
                    .setMessage("ایا میخواهید یادداشت به سطل زباله منتقل شود؟")
                    .setIcon(R.drawable.ic_delete)
                    .setNeutralButton("بله") { dialog, _ ->
                        val result = dao.editNotes( data.id, DBHelper.TRUE_STATE)
                        if (result){
                            toast("یادداشت به سطل زباله منتقل شد")
                            allData.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        }else{
                            toast("عملیات با مشکل مواجه شد")
                        }
                    }
                    .setPositiveButton("خیر") { _, _ -> }
                    .create()
                    .show()

            }

            view.root.setOnClickListener {
                val intent = Intent(context,AddNotesActivity::class.java)
                intent.putExtra("id",data.id)
                context.startActivity(intent)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun changData(data: ArrayList<RecyclerNotesModel>) {

            allData = data
            notifyDataSetChanged()

    }

    private fun toast(name: String) {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
    }

}