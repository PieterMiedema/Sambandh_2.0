package com.example.sambandh_20.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_home.*

class ProfileActivity : AppCompatActivity() {

    companion object{
        var currentUser: User? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
      //  supportActionBar?.title = null
        fetchCurrentUser()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setUserImage()
        fillProfile()
    }

    private fun fillProfile() {
        val spinner = findViewById<Spinner>(R.id.spinner_expected_length_stay)
        ArrayAdapter.createFromResource(
            this,
            R.array.list_length_stay,
            R.layout.custom_spinner_container
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
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

    private fun setUserImage() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        var profileImageUrl: String? = ""

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profileImageUrl = snapshot.child("profileImageUrL").getValue(String::class.java)
                if (profileImageUrl!!.isNotEmpty()){
                    Picasso.get().load(profileImageUrl).into(profile_image_profile)
                }
                else {
                    profile_image_profile.setImageResource(R.drawable.henk)
                }
            }

            override fun onCancelled(error: DatabaseError) {
//                sendToast("Something went wrong")
            }
        })
    }
}