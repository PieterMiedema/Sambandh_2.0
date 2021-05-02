package com.example.sambandh_20.ui.chat

import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_image_to_row_left.view.*

class ChatImageToItem(val mediaLink: String, val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val targetImageViewMedia = viewHolder.itemView.iv_chat_image_to_row_left
        val sentImageUrl = mediaLink
        if (sentImageUrl!!.isNotEmpty()){
            Picasso.get().load(sentImageUrl).into(targetImageViewMedia)
        }

        val targetImageView = viewHolder.itemView.iv_chat_to_row_left
        val profileImageUrl = user?.profileImageUrL
        if (profileImageUrl!!.isNotEmpty()){
            Picasso.get().load(profileImageUrl).into(targetImageView)
        }
        else {
            targetImageView.setImageResource(R.drawable.henk)
        }
    }
    override fun getLayout(): Int {
        return R.layout.chat_image_to_row_left
    }
}
