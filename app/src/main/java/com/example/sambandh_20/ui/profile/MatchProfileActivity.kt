package com.example.sambandh_20.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.example.sambandh_20.ui.chat.ChatActivity
import com.example.sambandh_20.ui.matches.MatchesOverviewActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_profile.*

class MatchProfileActivity : AppCompatActivity() {


    var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_profile)
        user = intent.getParcelableExtra<User>(MatchesOverviewActivity.USER_KEY)
        supportActionBar?.title = user?.displayName+"'s profile"
        Log.d("user", user.toString())

        fillProfile()

        gotochat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(MatchesOverviewActivity.USER_KEY, user)
            startActivity(intent)
        }
    }

    private fun fillProfile() {
        if (user?.profileImageUrL!!.isNotEmpty()){
            Picasso.get().load(user?.profileImageUrL).into(imageview_match_profile)
        }
        else {
            imageview_match_profile.setImageResource(R.drawable.henk)
        }
        tv_match_profile_name.text= user?.displayName
    }

}