package com.example.kotlinsql

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {


    class PersonViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.txtId)
        private var name = view.findViewById<TextView>(R.id.txtName)
        private var email = view.findViewById<TextView>(R.id.txtEmail)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(person: Person) {
            id.text = person.id.toString()
            name.text = person.name
            email.text = person.email
        }
    }

    private var personList: ArrayList<Person> = ArrayList()

    private var onClickItem: ((Person)->Unit)? = null
    private var onClickDeleteItem: ((Person)->Unit)? = null


    fun addItems(list: ArrayList<Person>) {
        personList = list
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: ((Person)->Unit)) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: ((Person)->Unit)) {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PersonViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
    )

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.bindView(person)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(person)}
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(person) }

    }

    override fun getItemCount(): Int {
        return personList.size
    }
}