package com.example.sambandh_20.ui.matches

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_matches_overview.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class MatchesOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches_overview)

        supportActionBar?.title ="Your Matches"
        val adapter = GroupAdapter<ViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())

        rv_matches_activity_overview.adapter = adapter

        fetchUser()
    }

    private fun fetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach({
                    Log.d("TEST", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                })
                rv_matches_activity_overview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
class UserItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_username_new_message.text = user.displayName
        if (user.profileImageUrL!!.isNotEmpty()){
            Picasso.get().load(user.profileImageUrL).into(viewHolder.itemView.iv_match_row)
        }
        else {
            viewHolder.itemView.iv_match_row.setImageResource(R.drawable.henk)
        }
//        Log.d("test", user.profileImageUrL)
//        Picasso.get().load(user.profileImageUrL).into(viewHolder.itemView.iv_new_message_row)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}