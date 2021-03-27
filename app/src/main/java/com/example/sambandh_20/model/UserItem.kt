package com.example.sambandh_20.model

import com.example.sambandh_20.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class UserItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_username_new_message.text = user.displayName
        Picasso.get().load(user.profileImageUrL).into(viewHolder.itemView.iv_new_message_row)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}