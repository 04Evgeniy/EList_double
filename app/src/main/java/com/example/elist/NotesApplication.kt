package com.example.elist

import android.app.Application
import androidx.room.Room
import com.example.elist.data.AppDatabase

class NotesApplication : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
        "main"
        ).build()
    }
}