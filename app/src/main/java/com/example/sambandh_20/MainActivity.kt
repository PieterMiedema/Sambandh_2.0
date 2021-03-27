package com.example.sambandh_20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sambandh_20.chat.ChatOverviewFragment
import com.example.sambandh_20.ui.home.HomeFragment
import com.example.sambandh_20.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyUserLoggedIn()
        setContentView(R.layout.activity_main)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view_bottom)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

        val homeFragment=HomeFragment()
        val chatOverviewFragment=ChatOverviewFragment()
    //    val thirdFragment=ThirdFragment()

       // setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottom_nav_menu_navigation_home->setCurrentFragment(homeFragment)
                R.id.bottom_nav_menu_navigation_chat->setCurrentFragment(chatOverviewFragment)
               // R.id.settings->setCurrentFragment(thirdFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment,fragment)
                commit()
            }




    fun verifyUserLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,
                        LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}