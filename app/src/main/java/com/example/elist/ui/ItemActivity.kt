package com.example.elist.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elist.Lists.EListItem
import com.example.elist.R
import com.example.elist.data.DataBaseHandler
import com.example.elist.data.INTENT_ELIST_ID
import com.example.elist.data.INTENT_ELIST_NAME
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {

    lateinit var dbHandler : DataBaseHandler
    var elistId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(item_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = intent.getStringExtra(INTENT_ELIST_NAME)

        elistId = intent.getLongExtra(INTENT_ELIST_ID, -1)
        dbHandler = DataBaseHandler(this)

        rv_item.layoutManager = LinearLayoutManager(this)

        but_edd_item.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Новая запись")

            val view = layoutInflater.inflate(R.layout.edit_text_dialog, null)
            val elistName = view.findViewById<EditText>(R.id.editv_elist)

            dialog.setView(view)
            dialog.setPositiveButton("Готово") { _: DialogInterface, _: Int ->
                if (elistName.text.isNotEmpty()) {
                    val item = EListItem()
                    item.itemName = elistName.text.toString()
                    item.eListId = elistId
                    item.isCompleted = false
                    dbHandler.addElistItem(item)
                    refreshList()
                }
            }
            dialog.setNegativeButton("Отмена") { _: DialogInterface, _: Int ->

            }
            dialog.show()
        }

    }

    fun updateItem(item: EListItem) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Изменить")

        val view = layoutInflater.inflate(R.layout.edit_text_dialog, null)
        val elistName = view.findViewById<EditText>(R.id.editv_elist)

        elistName.setText(item.itemName)

        dialog.setView(view)
        dialog.setPositiveButton("Готово") { _: DialogInterface, _: Int ->
            if (elistName.text.isNotEmpty()) {
                val item = EListItem()
                item.itemName = elistName.text.toString()
                item.eListId = elistId
                item.isCompleted = false
                dbHandler.updateElistItem(item)
                refreshList()
            }
        }
        dialog.setNegativeButton("Отмена") { _: DialogInterface, _: Int ->

        }
        dialog.show()
    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }

    private fun refreshList() {
        rv_item.adapter = ItemAdapter(
            this,
            dbHandler.getToEListItems(elistId)
        )
    }

    class ItemAdapter(val activity: ItemActivity, val list: MutableList<EListItem>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.text_view_item,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemName.text = list[position].itemName
            holder.itemName.isChecked = list[position].isCompleted
            holder.itemName.setOnClickListener {
                list[position].isCompleted = !list[position].isCompleted
                activity.dbHandler.updateElistItem(list[position])
            }

            holder.delete.setOnClickListener {
                val dialog = AlertDialog.Builder(activity)
//                dialog.setMessage("Подтвердите удаление")
                dialog.setTitle("Подтвердите удаление")
                dialog.setPositiveButton("Удалить") { _: DialogInterface, _: Int ->
                    activity.dbHandler.deleteEListItem(list[position].id)
                    activity.refreshList()
                }

                dialog.setNegativeButton("Отмена") { _: DialogInterface, _: Int ->
                }
                dialog.show()
            }

            holder.edit.setOnClickListener {
                activity.updateItem(list[position])
            }
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val itemName : CheckBox = v.findViewById(R.id.cb_item)

            val edit : ImageView = v.findViewById(R.id.iv_edit)
            val delete : ImageView = v.findViewById(R.id.iv_delete)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }

}