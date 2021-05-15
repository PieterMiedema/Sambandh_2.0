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
import android.widget.Button
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
    private var dateButton: Button? = null

    companion object{
        var minDate: String? = null
        var givenDate: String? = null
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
        dateButton = findViewById(R.id.datePickerButton);
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
        val dateOfBirth = givenDate
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()

        //field validation
        if (displayName.isEmpty() || /*dateOfBirth.isEmpty() ||*/ email.isEmpty() || password.isEmpty()) {
            sendToast("The required fields can not be empty"); return
        }
        if (!isUniqueDN(displayName)) { sendToast("Your Displayname is already taken"); return }
        //if (!dateOfBirth?.let { isDate(it) }!!) { sendToast("The date does not match the required format. Required format: dd/mm/yyyy"); return }
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
        val dateOfBirth = givenDate
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, profileImageUrL, displayName)//, dateOfBirth)
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
          //  date = SimpleDateFormat("dd/MM/yyyy").format(dateObject)
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

    //new stuff

    private fun initDatePicker() {
        val dateSetListener = OnDateSetListener { datePicker, year, month, day ->
            var month = month
            month = month + 1
            givenDate = makeDateString(day, month, year)
            dateButton!!.text = givenDate
        }
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR] -18
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        minDate = makeDateString(day,month, year)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)

    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        return getMonthFormat(day) + " " + month + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"

        //default should never happen
    }

    fun openDatePicker(view: View?) {
        datePickerDialog!!.show()
    }
}