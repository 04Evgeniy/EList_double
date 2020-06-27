package com.example.elist.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elist.Lists.EList
import com.example.elist.R
import com.example.elist.data.DataBaseHandler
import com.example.elist.data.INTENT_ELIST_ID
import com.example.elist.data.INTENT_ELIST_NAME
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dbHandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(dasboard_toolbar)
        title = "eList"

        dbHandler = DataBaseHandler(this)
        rv_dashboard.layoutManager = LinearLayoutManager(this)

        but_edd.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Новая категория")
            val view = layoutInflater.inflate(R.layout.edit_text_dialog, null)
            val elistName = view.findViewById<EditText>(R.id.editv_elist)

            dialog.setView(view)
            dialog.setPositiveButton("Готово") { _: DialogInterface, _: Int ->
                if (elistName.text.isNotEmpty()) {
                    val eList = EList()
                    eList.name = elistName.text.toString()
                    dbHandler.addEList(eList)
                    refreshList()
                }
            }
            dialog.setNegativeButton("Отмена") { _: DialogInterface, _: Int ->

            }
            dialog.show()
        }
    }

    fun updateEList(eList: EList) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Редактировать")
        val view = layoutInflater.inflate(R.layout.edit_text_dialog, null)
        val elistName = view.findViewById<EditText>(R.id.editv_elist)

        elistName.setText(eList.name)
        dialog.setView(view)
        dialog.setPositiveButton("Готово") { _: DialogInterface, _: Int ->
            if (elistName.text.isNotEmpty()) {
                eList.name = elistName.text.toString()
                dbHandler.updateList(eList)
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
        rv_dashboard.adapter = MainAdapter(this, dbHandler.geteLists())
    }



    class MainAdapter(val activity: MainActivity, val list: MutableList<EList>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.text_view_dialog, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.listName.text = list[position].name

            holder.listName.setOnClickListener {
                val intent = Intent(activity, ItemActivity::class.java)
                intent.putExtra(INTENT_ELIST_ID, list[position].id)
                intent.putExtra(INTENT_ELIST_NAME, list[position].name)
                activity.startActivity(intent)
            }

            holder.menu.setOnClickListener {
                val popup = PopupMenu(activity, holder.menu)
                popup.inflate(R.menu.menu)
                popup.setOnMenuItemClickListener {

                    when(it.itemId) {
                        R.id.menu_edit->{
                            activity.updateEList(list[position])
                        }

                        R.id.menu_delete->{
                            val dialog = AlertDialog.Builder(activity)
                            dialog.setTitle("Подтвердите удаление")
                            dialog.setPositiveButton("Удалить") { _: DialogInterface, _: Int ->
                                activity.dbHandler.deleteEList(list[position].id)
                                activity.refreshList()
                            }

                            dialog.setNegativeButton("Отмена") { _: DialogInterface, _: Int ->
                            }
                            dialog.show()
                        }

                        R.id.menu_completed->{
                            activity.dbHandler.completed(list[position].id, true)
                        }

                        R.id.menu_reset->{
                            activity.dbHandler.completed(list[position].id, false)
                        }
                    }

                    true
                }
                popup.show()
            }
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val listName : TextView = v.findViewById(R.id.textv_elist)
            val menu : ImageView = v.findViewById(R.id.iv_menu)
        }
    }
}
