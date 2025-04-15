package com.example.notebook.data.local.db.dao

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.notebook.data.local.db.DBHelper
import com.example.notebook.data.model.NotesModel
import com.example.notebook.data.model.RecyclerNotesModel

class NotesDao(private val db: DBHelper) {

    private val contentValues = ContentValues()
    private lateinit var cursor: Cursor


    fun editNotes(id: Int, state: String) : Boolean {

        val dataBase = db.writableDatabase
        contentValues.clear()
        contentValues.put(DBHelper.NOTES_DELETE_STATE, state)
        val result = dataBase.update(
            DBHelper.NOTES_TABLE,
            contentValues,
            "${DBHelper.NOTES_ID} = ?",
            arrayOf(id.toString())
        )
        dataBase.close()
        return result > 0

    }

    fun editNotes(id: Int, note: NotesModel) : Boolean {
        val dataBase = db.writableDatabase
        setContentValues(note)
        val result = dataBase.update(
            DBHelper.NOTES_TABLE,
            contentValues,
            "${DBHelper.NOTES_ID} = ?",
            arrayOf(id.toString())
        )
        dataBase.close()
        return result > 0


    }


    private fun setContentValues(note: NotesModel) {
        contentValues.clear()
        contentValues.put(DBHelper.NOTES_TITLE, note.title)
        contentValues.put(DBHelper.NOTES_DESCRIPTION, note.description)
        contentValues.put(DBHelper.NOTES_DELETE_STATE, note.deleteState)
    }

    fun saveNotes(notes: NotesModel): Boolean {


        val db = db.writableDatabase
        setContentValues(notes)
        val result = db.insert(DBHelper.NOTES_TABLE, null, contentValues)
        db.close()
        return result > 0

    }


    fun getNotesFromRecycler(value: String): ArrayList<RecyclerNotesModel> {
        val database = db.readableDatabase
        val query = "SELECT ${DBHelper.NOTES_ID}, ${DBHelper.NOTES_TITLE} " +
                "FROM ${DBHelper.NOTES_TABLE} " +
                "WHERE ${DBHelper.NOTES_DELETE_STATE} = ?"
        cursor = database.rawQuery(query, arrayOf(value))
        val data = getDataFromCursor()
        cursor.close()
        db.close()
        return data
    }


    private fun getDataFromCursor(): ArrayList<RecyclerNotesModel> {

        val data2 = ArrayList<RecyclerNotesModel>()

        try {

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(getIndex(DBHelper.NOTES_ID))
                    val title = cursor.getString(getIndex(DBHelper.NOTES_TITLE))
                    data2.add(RecyclerNotesModel(id, title))

                } while (cursor.moveToNext())

            }

        } catch (e: Exception) {
            Log.e("error", e.message.toString())
        }

        return data2
    }


    fun getNotesById(id: Int): NotesModel {

        val database = db.readableDatabase
        val query = "SELECT * FROM ${DBHelper.NOTES_TABLE} WHERE ${DBHelper.NOTES_ID} = ?"
        cursor = database.rawQuery(query, arrayOf(id.toString()))
        val data = getDataById()
        cursor.close()
        database.close()
        return data


    }

    private fun getDataById() : NotesModel {

        val data = NotesModel(0,"","","")
        try {

            if (cursor.moveToFirst()){
                    data.id = cursor.getInt(getIndex(DBHelper.NOTES_ID))
                    data.title = cursor.getString(getIndex(DBHelper.NOTES_TITLE))
                    data.description = cursor.getString(getIndex(DBHelper.NOTES_DESCRIPTION))
                    data.deleteState = cursor.getString(getIndex(DBHelper.NOTES_DELETE_STATE)) }

        }
        catch (e : Exception){

            Log.e("error",e.message.toString())

        }

        return data
    }


    private fun getIndex(name: String) = cursor.getColumnIndex(name)


}























//برای مثال ID INDEX = 0     TITLE INDEX = 1  تا ایندکس هارو نفهمه کرسر خبری از واکشی اطلاعات نیست چون کرسر ستون های جدول رو با اسم نمیشناسه

//نکته = راه بهینه تری برای این کار هست و این تمیز ترین راه نیست


//در رابطه با این فانکشن کمکی باید بگم که این کار فقد برای اینه که کرسر بدونه باید
// از کجا اطلاعات رو بخونه چون کرسر فقد با ایندکس های هر ستون کار میکنه نه اسم ستون اسم رو شناسایی نمیکنه

//اینجا ما مقدار دهی انجام نمیدیم فقد هر فیلد جدول را به یکی از ایتم های دیتا کلاس NotesModel نسبت میدیم

//در کل این وقتی فراخانی میشه که ما NotesModel را پر کردیم و حالا میخوام ان را به دیتا بیس اضافه کنیم

//نکته این هست که ما شرط های خود را در گوعری های sql تایین وضعیت نمیکنیم و اون رو به arrayOf
// پایین میسپاریم از اونجا تایین وضعیت میشه حالا هم دیلیت استیت رو سپردیم به value که ورودی هست