package com.example.sambandh_20.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.sambandh_20.R
import com.example.sambandh_20.model.User


class PreferencesFragment : Fragment(R.layout.fragment_preferences) {
    companion object{
        var currentUser: User?  = null
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
         fillSpinners()

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


}