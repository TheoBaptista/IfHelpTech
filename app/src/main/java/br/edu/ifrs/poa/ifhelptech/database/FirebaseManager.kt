package br.edu.ifrs.poa.ifhelptech.database

import com.google.firebase.database.FirebaseDatabase

object FirebaseManager {
    private lateinit var database: FirebaseDatabase

    fun initialize() {
        database = FirebaseDatabase.getInstance()
    }

    fun getDatabase(): FirebaseDatabase {
        return database
    }
}