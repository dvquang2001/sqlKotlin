package com.example.kotlinsql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var rcvPerson : RecyclerView
    private var personAdapter: PersonAdapter? = null
    private var person: Person? = null

    private lateinit var mySqlite: MySqlite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addControls()
        initRecyclerView()
        mySqlite = MySqlite(this)
        btnAdd.setOnClickListener { addPerson() }
        btnView.setOnClickListener { getPerson() }
        btnUpdate.setOnClickListener { updatePerson() }
        personAdapter?.setOnClickItem {
            Toast.makeText(this,it.name,Toast.LENGTH_LONG).show()

            edtName.setText(it.name)
            edtEmail.setText(it.email)
            person = it
        }

        personAdapter?.setOnClickDeleteItem { deletePerson(it.id) }

    }

    private fun deletePerson(id: Int) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") {dialog,_ ->
            mySqlite.deletePersonById(id)
            getPerson()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") {dialog,_ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()

    }

    private fun updatePerson() {
        val name = edtName.text.toString()
        val email = edtEmail.text.toString()

        if(name==person?.name && email==person?.email) {
            Toast.makeText(this," Infor not change...",Toast.LENGTH_LONG).show()
            return
        }

        if(person == null) return

        val _person = Person(id = person!!.id,name = name,email = email)
        val status = mySqlite.updatePerson(_person)
        if(status > -1){
            clearEditText()
            getPerson()
        }else{
            Toast.makeText(this,"Update false",Toast.LENGTH_LONG).show()
        }
    }

    private fun getPerson() {
        val personList = mySqlite.getAllPerson()
        personAdapter?.addItems(personList)
    }

    private fun addPerson() {
        val name = edtName.text.toString()
        val email = edtEmail.text.toString()

        if(name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this,"Please enter valid value",Toast.LENGTH_LONG).show()
        }else {
            val person = Person(name = name,email =  email)
            val status = mySqlite.insertPerson(person)

            if(status > -1) {
                Toast.makeText(this,"add success",Toast.LENGTH_LONG).show()
                clearEditText()
                getPerson()
            } else{
                Toast.makeText(this,"add false",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearEditText() {
        edtName.setText("")
        edtEmail.setText("")
        edtName.requestFocus()
    }

    private fun initRecyclerView() {
        rcvPerson.layoutManager = LinearLayoutManager(this)
        personAdapter = PersonAdapter()
        rcvPerson.adapter = personAdapter
    }

    private fun addControls() {
        edtEmail    = findViewById(R.id.edtEmail)
        edtName     = findViewById(R.id.edtName)
        btnAdd      = findViewById(R.id.btnAdd)
        btnView     = findViewById(R.id.btnView)
        btnUpdate   = findViewById(R.id.btnUpdate)

        rcvPerson   = findViewById(R.id.rcvPerson)

    }
}