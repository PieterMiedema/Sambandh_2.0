package com.example.sambandh_20.model

import com.example.sambandh_20.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row_right.view.*


class ChatToItem(val text: String, val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_chat_to_row_right.text = text
        val uri = user.profileImageUrL
        val targetImageView = viewHolder.itemView.iv_chat_to_row_right
        Picasso.get().load(uri).into(targetImageView)

    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row_right
    }
}