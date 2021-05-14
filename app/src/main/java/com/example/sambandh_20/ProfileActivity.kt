package com.example.sambandh_20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sambandh_20.model.User
import com.example.sambandh_20.ui.chat.ChatActivity
import com.example.sambandh_20.ui.matches.MatchesOverviewActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    companion object{
        var currentUser: User? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
      //  supportActionBar?.title = null
        fetchCurrentUser()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


    }
    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                supportActionBar?.title = currentUser?.displayName + "'s profile"
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}