package com.example.sambandh_20.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.core.util.Predicate
import androidx.fragment.app.Fragment
import com.example.sambandh_20.MainActivity
import com.example.sambandh_20.R
import com.example.sambandh_20.model.Preferences
import com.example.sambandh_20.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_preferences.*
import java.lang.Exception
import java.nio.channels.InterruptedByTimeoutException


class PreferencesFragment : Fragment(R.layout.fragment_preferences) {
    companion object{
        var currentPreferences: Preferences?  = null
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
         fetchCurrentPreferences()

         btn_save_pref.setOnClickListener {
             save()
         }
    }

    private fun fillSpinners() {
        val spinnerGender = requireView().findViewById<Spinner>(R.id.spinner_gender_pref)
        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.genders_pref,
                R.layout.custom_spinner_container
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGender.adapter = adapter
        }

        val spinnerLenghtOfStay = requireView().findViewById<Spinner>(R.id.spinner_expected_length_stay_pref)
        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_length_stay,
                R.layout.custom_spinner_container
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerLenghtOfStay.adapter = adapter
        }
    }

    private fun save() {
        val minAge = et_pref_min_age.text.toString()
        val maxAge = et_pref_max_age.text.toString()

        //field validation
        if (!(minAge.isDigitsOnly() || minAge.isEmpty()) || !(maxAge.isDigitsOnly() || maxAge.isEmpty())) {
            sendToast("Minimum or maximum age must be a number"); return
        }

        savePreferencesToFirebaseDatabase()
    }

    private fun savePreferencesToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val gender = spinner_gender_pref.selectedItemId.toInt()
        val minAge = et_pref_min_age.text.toString()
        val maxAge = et_pref_max_age.text.toString()
        val regionOfOrigin = et_pref_region_origin.text.toString()
        val religion = et_pref_religion.text.toString()
        val currentResidence = et_pref_current_residence.text.toString()
        val expectedStay = spinner_expected_length_stay_pref.selectedItemId.toInt()
        val hobbies = et_pref_hobbies.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/preferences/$uid")

        val pref = Preferences(uid, gender, minAge, maxAge, regionOfOrigin, religion, currentResidence, expectedStay, hobbies)
        ref.setValue(pref)
                .addOnSuccessListener {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
    }

    private fun fillPreferences() {
        fillSpinners()
        et_pref_min_age.setText(currentPreferences?.minAge, TextView.BufferType.EDITABLE)
        et_pref_max_age.setText(currentPreferences?.maxAge, TextView.BufferType.EDITABLE)
        et_pref_region_origin.setText(currentPreferences?.regionOfOrigin, TextView.BufferType.EDITABLE)
        et_pref_religion.setText(currentPreferences?.religion, TextView.BufferType.EDITABLE)
        et_pref_current_residence.setText(currentPreferences?.currentResidence, TextView.BufferType.EDITABLE)
        et_pref_hobbies.setText(currentPreferences?.hobbies, TextView.BufferType.EDITABLE)

        if (currentPreferences?.expectedStay != null) {
            spinner_expected_length_stay_pref.setSelection(currentPreferences?.expectedStay!!)
        }
        if (currentPreferences?.gender != null) {
            spinner_gender_pref.setSelection(currentPreferences?.gender!!)
        }
    }

    private fun fetchCurrentPreferences() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/preferences/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentPreferences = snapshot.getValue(Preferences::class.java)
                fillPreferences()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //sends short toast message to the user
    fun sendToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}