package com.example.elist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: Note)  //если таблицы нет, он ее создает

    @Delete
    fun deleteNote(note: Note)

    @Query("select * from notes") //перечисляем список полей для их получения
    fun getAllNotes(): List<Note>
}