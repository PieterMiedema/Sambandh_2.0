package com.example.sambandh_20.ui.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sambandh_20.MainActivity
import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title ="Register"
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            register()
        }

        tv_already_account.setOnClickListener {
            finish()
        }

        btn_image_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            select_phote_imageview_register.setImageBitmap(bitmap)
            btn_image_register.alpha = 0f
        }
    }

    fun register() {
        val displayName = etDisplayNameRegister.text.toString()
        val dateOfBirth = etDateOfBirthRegister.text.toString()
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()

        //field validation
        if (displayName.isEmpty() || /*dateOfBirth.isEmpty() ||*/ email.isEmpty() || password.isEmpty()) {
            sendToast("The required fields can not be empty"); return
        }
        if (!isUniqueDN(displayName)) { sendToast("Your Displayname is already taken"); return }
        if (!isDate(dateOfBirth)) { sendToast("The date does not match the required format. Required format: dd/mm/yyyy"); return }
        //if (selectedPhotoUri == null) { sendToast("Please select a photo") }

        //create account
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                upLoadImageToFirebaseStorage()
            }
            .addOnFailureListener { sendToast("Failed to create user: ${it.message}") }
    }

    private fun upLoadImageToFirebaseStorage(){
        if (selectedPhotoUri == null) {
            saveUserToFirebaseDatabase("")
            return
        }
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrL: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val displayName = etDisplayNameRegister.text.toString()
        val dateOfBirth = etDateOfBirthRegister.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, profileImageUrL, displayName,  dateOfBirth)
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }

    //checks if the date is in the correct format
    private fun isDate(dateOfBirth: String): Boolean {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val dateObject: Date

        try{
            var date= etDateOfBirthRegister.text.toString()
            dateObject = formatter.parse(date)
            date = SimpleDateFormat("dd/MM/yyyy").format(dateObject)
        }
        catch (ex: Exception) {
            return false
        }
        return true
    }

    //checks is display name is unique
    private fun isUniqueDN(dn: String): Boolean {
        return true
    }

    //sends short toast message to the user
    fun sendToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}