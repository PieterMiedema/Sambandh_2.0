package com.example.sambandh_20.ui.chat

import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_message_from_row_right.view.*

class ChatFromItem(val text: String,  val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_chat_from_row_right.text = text

        val targetImageView = viewHolder.itemView.iv_chat_from_row_right
        val profileImageUrl = user?.profileImageUrL
        if (profileImageUrl!!.isNotEmpty()){
            Picasso.get().load(profileImageUrl).into(targetImageView)
        }
        else {
            targetImageView.setImageResource(R.drawable.henk)
        }
    }
    override fun getLayout(): Int {
        return R.layout.chat_message_from_row_right
    }
}