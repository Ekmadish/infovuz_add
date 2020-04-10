package com.info.infoadder.Activity

import com.info.infoadder.Adpter.InfoViewHolder
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.info.infoadder.R
import com.info.infoadder.module.Info

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.DateFormat
import java.util.*


const val TITLE_PATH = "title"
const val TITLE_CONTENT = "ed_text"
const val TIMESTAMP_PATH = "timestamp"
class MainActivity : AppCompatActivity() {
    private val InfoRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(PATH)
    companion object{
        val INFO_KEY="INFO_KEY"
    }
    private var activeInfo : Info?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recyclerView_main.setHasFixedSize(true)
        recyclerView_main.layoutManager= LinearLayoutManager(this)
        val recyclerOptions = getRecyclerOptions(this)
        recyclerView_main.adapter= object : FirebaseRecyclerAdapter<Info, InfoViewHolder>(recyclerOptions)
        {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_info, parent, false)
                return InfoViewHolder(view)
            }

            override fun onBindViewHolder(viewHolder: InfoViewHolder, p1: Int, p2: Info) {
               // val onEditClickListener = View.OnClickListener {
                 //   showDialog(Info)}
               // val onDeleteClickListener = View.OnClickListener { dbHelper.deleteTodo(todo) }
               // viewHolder.bind(Info, onEditClickListener, onDeleteClickListener)
            }}




        fab.setOnClickListener { view ->
            startActivity(Intent(this,Add::class.java))
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }





    }
    fun getRecyclerOptions(owner: LifecycleOwner) = FirebaseRecyclerOptions.Builder<Info>()
        .setQuery(InfoRef.orderByKey(), Info::class.java)
        .setLifecycleOwner(owner)
        .build()
    private fun getTimestamp(): String {
        val date = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateTimeInstance()
        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
