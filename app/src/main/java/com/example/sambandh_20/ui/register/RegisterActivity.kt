package com.example.sambandh_20.ui.register

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
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
    private var datePickerDialog: DatePickerDialog? = null
    private var dateButton: TextView? = null
    var selectedPhotoUri: Uri? = null

    companion object{
        var minDate: String = ""
        var givenDate: String = ""
    }

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

        initDatePicker();
        dateButton = findViewById(R.id.etDateOfBirthRegister);
    }

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
        val dateOfBirth = givenDate
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()

        //field validation
        if (displayName.isEmpty() || dateOfBirth.isNullOrEmpty() || email.isEmpty() || password.isEmpty()) {
            sendToast("The required fields can not be empty"); return
        }
        if (!isOldEnough(dateOfBirth, minDate)) { sendToast("You are too young to use this app."); return }

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
        val dateOfBirth = givenDate
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, profileImageUrL, displayName, dateOfBirth, "", "", "", null, "", "")
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }

    //compares dateStrings
    private fun isOldEnough(date: String?, dateMax: String?): Boolean {
        val formatter = SimpleDateFormat("dd-MM-yyyy")

        try{
            if (!date.isNullOrBlank() || !dateMax.isNullOrBlank()) {
                val dateObject = formatter.parse(date)
                val dateLimit = formatter.parse(dateMax)
                dateLimit.seconds = 1
                return dateObject.before(dateLimit)!!
            }
        }
        catch (ex: Exception) {
            return false
        }
        return false
    }

    private fun initDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR] - 18
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        minDate = makeDateString(day, (month + 1), year)

        val dateSetListener = OnDateSetListener { datePicker, year, month, day ->
            var month = month
            month += 1
            givenDate = makeDateString(day, month, year)
            dateButton!!.text = givenDate
        }
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        var dayStr = day.toString()
        var monthStr = month.toString()
        if(dayStr.length == 1) { dayStr = "0" + dayStr; }
        if(monthStr.length == 1) { monthStr = "0" + monthStr; }
        return day.toString() + "-" + month.toString() + "-" + year.toString()
    }

    fun openDatePicker(view: View?) {
        datePickerDialog!!.show()
    }

    //sends short toast message to the user
    fun sendToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}