package com.example.elist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    val text: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

//{
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Note
//
//        if (text != other.text) return false
//        if (id != other.id) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = text.hashCode()
//        result = 31 * result + text.hashCode()
//        result = 31 * result + id.hashCode()
//        return result
//    }
//}