package com.info.infoadder.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.database.*
import com.info.infoadder.R
import com.info.infoadder.module.Info
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_info.view.*

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
        supportActionBar?.title="demo"
        setSupportActionBar(toolbar)
        recyclerView_main.setHasFixedSize(true)
        fab.setOnClickListener { view ->
            startActivity(Intent(this,Add::class.java))
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
                }

        recyclerView_main.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        fetchInfo()
    }

    private fun fetchInfo() {
        InfoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {            }
            override fun onDataChange(p0: DataSnapshot)
            {   val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach{
                    Log.d("main",it.toString())
                    val info =it.getValue(Info::class.java)
                    if (info!=null){
                        adapter.add(InfoItem(info))
                    }}
                adapter.setOnItemClickListener{item, view ->
                    val _infoItem =item as InfoItem
                    Toast.makeText(this@MainActivity,"Works",Toast.LENGTH_SHORT).show()
                }
                recyclerView_main.adapter=adapter
            }
        })
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
            R.id.action_refresh -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
class InfoItem(val info: Info): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.textView_title_row.text=  info.name
        viewHolder.itemView.textView_discrption_row.text=info.discriptin
        Picasso.get().load(info.image).into(viewHolder.itemView.imageView_row)

    }

    override fun getLayout(): Int {
        return R.layout.row_info

    }
}
