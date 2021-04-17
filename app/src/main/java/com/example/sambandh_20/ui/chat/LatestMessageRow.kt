package com.example.sambandh_20.ui.chat

import com.example.sambandh_20.R
import com.example.sambandh_20.model.ChatMessage
import com.example.sambandh_20.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_item.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>() {
    var chatPartnerUser:  User? = null
    override fun getLayout(): Int {
        return R.layout.latest_message_item
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_latest_massage_latest_message.text = chatMessage.text
        val chatPatnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatPatnerId = chatMessage.toId
        } else {
            chatPatnerId = chatMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPatnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.tv_username_latest_message_item.text = chatPartnerUser?.displayName
                val targetImageView = viewHolder.itemView.iv_latest_messages
                val profileImageUrl = chatPartnerUser?.profileImageUrL
                if (profileImageUrl!!.isNotEmpty()){
                    Picasso.get().load(profileImageUrl).into(targetImageView)
                }
                else {
                    targetImageView.setImageResource(R.drawable.henk)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}