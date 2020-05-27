package com.example.elist.data

import com.example.elist.NotesApplication

class NoteRepository {
    private val noteDao = NotesApplication.db.getNoteDao() //сохраняется реализация, для дальнейшего использования

    fun createNewNote(text: String) = noteDao.insertNote(Note(text))

    fun getAllNotes() = noteDao.getAllNotes() // переадрессовывает вызов в NoteDao
}