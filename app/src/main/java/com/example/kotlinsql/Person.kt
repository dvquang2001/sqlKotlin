package com.example.kotlinsql

import java.util.*

data class Person(
    var id: Int = getAutoID(),
    var name: String = "",
    var email: String = ""
) {

    companion object {
        fun getAutoID(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}