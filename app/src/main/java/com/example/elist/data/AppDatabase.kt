package com.example.elist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1) // берется таблица из БД
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}