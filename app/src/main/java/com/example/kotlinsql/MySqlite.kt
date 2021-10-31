package com.example.kotlinsql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySqlite(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Person.sqlite"
        private const val TABLE_NAME = "Person"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME +"(" +
                            ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                            + EMAIL + " TEXT" + ")")
        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPerson(person: Person): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,person.id)
        contentValues.put(NAME,person.name)
        contentValues.put(EMAIL,person.email)

        val success = db.insert(TABLE_NAME,null,contentValues)
        db.close()
        return success
    }

    fun getAllPerson(): ArrayList<Person> {
        val personList: ArrayList<Person> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (ex: Exception){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                email = cursor.getString(2)

                val person = Person(id = id,name = name,email = email)
                personList.add(person)
            }while (cursor.moveToNext())
        }

        return personList
    }

    fun updatePerson(person: Person): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,person.id)
        contentValues.put(NAME,person.name)
        contentValues.put(EMAIL,person.email)

        val success = db.update(TABLE_NAME,contentValues,"id="+person.id,null)
        db.close()
        return success
    }

    fun deletePersonById(id: Int): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,id)

        val success = db.delete(TABLE_NAME,"id="+id,null)
        db.close()
        return success
    }
}