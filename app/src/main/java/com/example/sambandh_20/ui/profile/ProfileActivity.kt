package com.example.sambandh_20.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import com.example.sambandh_20.MainActivity
import com.example.sambandh_20.R
import com.example.sambandh_20.model.User
import com.example.sambandh_20.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class ProfileActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null

    companion object{
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
      //  supportActionBar?.title = null
        super.onCreate(null)
        setContentView(R.layout.activity_profile)
        fetchCurrentUser()

        profile_image_profile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
        }

        btn_save_profile.setOnClickListener {
            save()
        }
    }

    private fun fillProfile() {
        fillSpinner()
        setUserImage()
        et_profile_display_name.setText(currentUser?.displayName, TextView.BufferType.EDITABLE)
        et_profile_region_origin.setText(currentUser?.regionOfOrigin, TextView.BufferType.EDITABLE)
        et_profile_religion.setText(currentUser?.religion, TextView.BufferType.EDITABLE)
        et_profile_current_residence.setText(currentUser?.currentResidence, TextView.BufferType.EDITABLE)
        et_profile_hobbies.setText(currentUser?.hobbies, TextView.BufferType.EDITABLE)
        et_profile_biography.setText(currentUser?.biography, TextView.BufferType.EDITABLE)
        if (currentUser?.expectedStay != null) {
            spinner_expected_length_stay.setSelection(currentUser?.expectedStay!!)
        }
    }

    private fun save() {
        val displayName = et_profile_display_name.text.toString()

        //field validation
        if (displayName.isEmpty()) {
            sendToast("Display name can not be empty"); return
        }

        upLoadImageToFirebaseStorage()
    }

    private fun fillSpinner() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            profile_image_profile.setImageBitmap(bitmap)
        }
    }

    private fun upLoadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) {
            saveUserToFirebaseDatabase("")
            return
        }

        if (!currentUser?.profileImageUrL.isNullOrBlank()) {
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
        else {
            saveUserToFirebaseDatabase("")
        }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrL: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val displayName = et_profile_display_name.text.toString()
        val dateOfBirth = currentUser?.dateOfBirth!!
        val regionOfOrigin = et_profile_region_origin.text.toString()
        val religion = et_profile_religion.text.toString()
        val currentResidence = et_profile_current_residence.text.toString()
        val expectedStay = spinner_expected_length_stay.selectedItemId.toInt()
        val hobbies = et_profile_hobbies.text.toString()
        val biography = et_profile_biography.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        var actualProfileImageURL = profileImageUrL
        if (currentUser?.profileImageUrL!!.isNotBlank() && profileImageUrL.isBlank()) {
            actualProfileImageURL = currentUser?.profileImageUrL!!
        }

        val user = User(uid, actualProfileImageURL, displayName, dateOfBirth, regionOfOrigin, religion, currentResidence, expectedStay, hobbies, biography)
        ref.setValue(user)
                .addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                supportActionBar?.title = currentUser?.displayName + "'s profile"
                fillProfile()
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

    //sends short toast message to the user
    fun sendToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}