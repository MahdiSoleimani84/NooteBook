package com.example.notebook.adapter.recycler

import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.R
import com.example.notebook.data.local.db.DBHelper
import com.example.notebook.data.local.db.dao.NotesDao
import com.example.notebook.data.model.RecyclerNotesModel
import com.example.notebook.databinding.ListItemBinNotesBinding


class RecycleHolder
    (
    private val context: Context,
    private val dao: NotesDao

) : RecyclerView.Adapter<RecycleHolder.RecycleViewHolder>() {



    private var allData = dao.getNotesFromRecycler(DBHelper.TRUE_STATE)






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecycleViewHolder(
            ListItemBinNotesBinding.inflate(LayoutInflater.from(context), parent, false)

        )

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) =
        holder.setData(allData[position])


    override fun getItemCount() = allData.size





    inner class RecycleViewHolder
        (private val view: ListItemBinNotesBinding)
        : RecyclerView.ViewHolder(view.root){

        fun setData(data: RecyclerNotesModel) {

            view.txtTitleNotes.text = data.title

            view.imgDeleteNotes.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
                    .setTitle("حدف یادداشت")
                    .setMessage("ایا میخواهید یادداشت حذف شود؟")
                    .setIcon(R.drawable.ic_delete)
                    .setNeutralButton("بله") { dialog, _ ->
                        val result = dao.deleteNotes(data.id)
                        if (result){
                            toast("یادداشت حدف شد")
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

            view.imgDeleteBinNotes.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
                    .setTitle("بازگردانی یادداشت")
                    .setMessage("ایا میخواهید یادداشت بازگردانی شود؟")
                    .setIcon(R.drawable.ic_delete)
                    .setNeutralButton("بله") { dialog, _ ->
                        val result = dao.editNotes( data.id, DBHelper.FALSE_STATE)
                        if (result){
                            toast("یادداشت بازگردانی شد")
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

        }
    }



    private fun toast(name: String) {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
    }

}