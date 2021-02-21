package com.example.sambandh_20.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sambandh_20.MainActivity
import com.example.sambandh_20.R
import com.example.sambandh_20.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title ="Login"

        btn_login.setOnClickListener{
            login()
        }
        tv_dont_have_account.setOnClickListener {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        }
    }

    private fun login() {
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    sendToast("Failed to login: ${it.message}")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
    }

    //sends short toast message to the user
    fun sendToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}