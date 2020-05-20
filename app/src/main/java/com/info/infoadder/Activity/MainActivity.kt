package com.info.infoadder.Activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.cutsom_layout.*
import kotlinx.android.synthetic.main.row_info.view.*
import org.jetbrains.anko.doAsync

const val TITLE_PATH = "title"
const val TITLE_CONTENT = "ed_text"
const val TIMESTAMP_PATH = "timestamp"
class MainActivity : AppCompatActivity() {
    private val InfoRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(PATH)
    companion object{
        val INFO_KEY="INFO_KEY"

    }
    val about="thankseverthing"

    val adapter = GroupAdapter<GroupieViewHolder>()
    private var activeInfo : Info?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



doAsync {
   }
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
            {
                p0.children.forEach{
                    Log.d("main",it.toString())
                    val info =it.getValue(Info::class.java)
                    if (info!=null){
                        adapter.add(InfoItem(info,adapter))
                    }}


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
        return when (item.itemId) {
            R.id.action_refresh -> {
                showDialog(about)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.cutsom_layout)

        val one=dialog.findViewById<ImageView>(R.id.imageView1)
        val tow=dialog.findViewById<ImageView>(R.id.imageView2)
        val tree=dialog.findViewById<ImageView>(R.id.imageView3)
        Picasso.get().load("https://sun9-19.userapi.com/impf/c844418/v844418764/101e4e/skPQE7u8cis.jpg?size=200x0&quality=90&sign=bd36560ce80719a08ba1bdddd6255b79").into(one)
        Picasso.get().load("https://sun9-44.userapi.com/impf/c840225/v840225435/7c7bf/KGmqQGZmjks.jpg?size=200x0&quality=90&sign=1a479cbb231c321bbc6fa24edaa7ab33").into(tow)
        Picasso.get().load("https://sun9-9.userapi.com/impg/c858332/v858332030/11e80f/QBqPdaD-giA.jpg?size=200x0&quality=90&sign=20c34409cd370ec3bd3c46e73eb7b122").into(tree)

        val body = dialog.findViewById(R.id.tvBody) as TextView
        body.text = title
//        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
       val noBtn = dialog.findViewById(R.id.btn_no) as TextView
//        yesBtn.setOnClickListener {
//            dialog.dismiss()
//        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }
}



class InfoItem(val info: Info,val adapter: GroupAdapter<GroupieViewHolder>): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    viewHolder.itemView.textView_title_row.text=  info.name
        viewHolder.itemView.textView_discrption_row.text=info.discriptin
        Picasso.get().load(info.image).into(viewHolder.itemView.imageView_row)

viewHolder.itemView.setOnClickListener {
    it.setOnLongClickListener {
        Toast.makeText(viewHolder.itemView.context, "Long click detected", Toast.LENGTH_SHORT).show()
        AlertDialog.Builder(viewHolder.itemView.context)
            .setTitle("Delete")
            .setMessage("are you sure?")
            .setPositiveButton(android.R.string.yes) { _, _ -> deleteItem(adapter ,position) }

            .setNegativeButton(android.R.string.no) { _, _ ->  }
            .show()



        true
    }
}

    }





    override fun getLayout(): Int {
        return R.layout.row_info

    }


private fun deleteItem(adapter: GroupAdapter<GroupieViewHolder>,position: Int) {
    val ref = FirebaseDatabase.getInstance().getReference("/info/")
    ref.child(info.name).removeValue()

    adapter.removeGroupAtAdapterPosition(position)
}
}
