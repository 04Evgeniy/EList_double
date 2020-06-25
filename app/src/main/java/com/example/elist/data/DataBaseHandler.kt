package com.example.elist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.elist.Lists.EList
import com.example.elist.Lists.EListItem

class DataBaseHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
                "CREATE TABLE $TABLE_ELIST (" +
                "$COL_ID integer," +
                "$COL_CREATED_AT DATE," +
                "$COL_NAME varchar(255)," +
                "CONSTRAINT eList_pk PRIMARY KEY (id)" +
                ");"

        val createItemTable =
                "CREATE TABLE $TABLE_ELIST_ITEM (" +
                "$COL_ID int," +
                "$COL_CREATED_AT DATE," +
                "$COL_ELIST_ID integer," +
                "$COL_ITEM_NAME varchar(255)," +
                "$COL_IS_COMPLETED integer," +
                "CONSTRAINT eListitem_pk PRIMARY KEY (id)" +
                ");"

        db.execSQL(createTable)
        db.execSQL(createItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

        fun addEList(elist: EList) : Boolean {      //eListing - elist
            val db = writableDatabase
            val cv = ContentValues()
            cv.put(COL_NAME, elist.name)
            val result = db.insert(TABLE_ELIST, null, cv)
            return result!=(-1).toLong()
        }

        fun updateList(elist: EList) {      //eListing - elist
            val db = writableDatabase
            val cv = ContentValues()
            cv.put(COL_NAME, elist.name)
            db.update(TABLE_ELIST, cv, "$COL_ID=?", arrayOf(elist.id.toString()))
        }

        fun deleteEList(eListId: Long) {
            val db = writableDatabase
            db.delete(TABLE_ELIST_ITEM, "$COL_ELIST_ID=?", arrayOf(eListId.toString()))
            db.delete(TABLE_ELIST, "$COL_ID=?", arrayOf(eListId.toString()))
        }

        fun completed(eListId: Long, isCompleted: Boolean){
            val db = writableDatabase
            val queryResult = db.rawQuery("SELECT * FROM $TABLE_ELIST_ITEM WHERE $COL_ELIST_ID = $eListId", null)

            if (queryResult.moveToFirst()) {
                do {
                    val item = EListItem()
                    item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                    item.eListId = queryResult.getLong(queryResult.getColumnIndex(COL_ELIST_ID))
                    item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME))
                    item.isCompleted = isCompleted
                    updateElistItem(item)

                } while (queryResult.moveToNext())
            }
            queryResult.close()
        }



        fun geteLists() : MutableList<EList>{
            val result : MutableList<EList> = ArrayList()
            val db = readableDatabase
            val queryResult = db.rawQuery("SELECT * from $TABLE_ELIST", null)
            if (queryResult.moveToFirst()) {
                do {
                    val elist = EList()
                    elist.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                    elist.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                    result.add(elist)

                } while (queryResult.moveToNext())
            }
            queryResult.close()
            return result
        }

    fun addElistItem(item: EListItem) : Boolean{
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, item.itemName)
        cv.put(COL_ELIST_ID, item.eListId)
        cv.put(COL_IS_COMPLETED, item.isCompleted)

        val result = db.insert(TABLE_ELIST_ITEM, null, cv)
        return result != (-1).toLong()
    }

    fun updateElistItem(item: EListItem) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, item.itemName)
        cv.put(COL_ELIST_ID, item.eListId)
        cv.put(COL_IS_COMPLETED, item.isCompleted)

        db.update(TABLE_ELIST_ITEM, cv, "$COL_ID=?", arrayOf(item.id.toString()))
    }

    fun deleteEListItem(itemId : Long) {
        val db = writableDatabase
        db.delete(TABLE_ELIST_ITEM, "$COL_ID=?", arrayOf(itemId.toString()))
    }

    fun getToEListItems(eListId : Long) : MutableList<EListItem> {
        val result: MutableList<EListItem> = ArrayList()

        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_ELIST_ITEM WHERE $COL_ELIST_ID = $eListId", null)

        if (queryResult.moveToFirst()) {
            do {
                val item = EListItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.eListId = queryResult.getLong(queryResult.getColumnIndex(COL_ELIST_ID))
                item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME))
                item.isCompleted = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1
                result.add(item)

            } while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }

}