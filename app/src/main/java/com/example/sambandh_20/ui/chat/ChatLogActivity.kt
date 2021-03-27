package com.example.sambandh_20.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sambandh_20.R
import com.example.sambandh_20.model.ChatFromItem
import com.example.sambandh_20.model.ChatMessage
import com.example.sambandh_20.model.ChatToItem
import com.example.sambandh_20.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
class ChatLogActivity : AppCompatActivity() {
    companion object {
        val TAG = "ChatLogActivity"
    }

    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        rv_chat_log.adapter = adapter
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.displayName
        //    setupDummyData()
        ListenForMessages()
        btn_send_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message")
            performSendMessage()
        }
    }
    private fun ListenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = ChatOverviewFragment.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else {
                        //val toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }
                rv_chat_log.scrollToPosition(adapter.itemCount -1)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun performSendMessage() {
        val text = et_chat_log.text.toString()
        // Pushes to messages
        // val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user!!.uid
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        if (fromId == null) return
        val chatMessage =
            ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                et_chat_log.text.clear()
                rv_chat_log.scrollToPosition(adapter.itemCount -1)
            }
        toReference.setValue(chatMessage)
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)
        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }
//    private fun setupDummyData() {
//        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(ChatFromItem("from MESSAGES"))
//        adapter.add(ChatToItem("TO MESSAGE"))
//        adapter.add(ChatFromItem("YEAHHHHHHHHH"))
//        adapter.add(ChatToItem("HELLOOOO"))
//        adapter.add(ChatFromItem("BOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"))
//
//
//        rv_chat_log.adapter = adapter
//    }
}