package com.example.sambandh_20.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.example.sambandh_20.ui.matches.MatchesOverviewActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match.*
import java.io.Console

class MatchActivity : AppCompatActivity() {

    var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        user = intent.getParcelableExtra<User>(MatchesOverviewActivity.USER_KEY)
        supportActionBar?.title = user?.displayName+"'s profile"
        Log.d("user", user.toString())
        fillProfile()
    }

    private fun fillProfile() {
        if (user?.profileImageUrL!!.isNotEmpty()){
            Picasso.get().load(user?.profileImageUrL).into(imageview_match_profile)
        }
        else {
            imageview_match_profile.setImageResource(R.drawable.henk)
        }
    }

}