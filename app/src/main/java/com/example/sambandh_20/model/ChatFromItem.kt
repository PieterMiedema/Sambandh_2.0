package com.example.sambandh_20.model

import com.example.sambandh_20.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row_left.view.*

class ChatFromItem(val text: String,  val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_chat_from_row_right.text = text
        //load image into chat
        val uri = user.profileImageUrL
        val targetImageView = viewHolder.itemView.iv_chat_from_row_left
        Picasso.get().load(uri).into(targetImageView)

    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row_left
    }
}