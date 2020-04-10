package com.info.infoadder.Adpter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewAnimationUtils
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import com.info.infoadder.module.Info

import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_info.*
import kotlin.time.seconds
class InfoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView)
    , LayoutContainer {
    fun bind(info: Info,editClickListener: View.OnClickListener,deleteClickListener: View.OnClickListener) {
        val id_n:String

        containerView.tag = todo.id
        titleTw.text = todo.title
        id_text.setText(adapterPosition .plus(1).toString())
        // id_text.text = todo.timestamp

//        editBtn.setOnClickListener(editClickListener)
//        delBtn.setOnClickListener(deleteClickListener)
    }
}