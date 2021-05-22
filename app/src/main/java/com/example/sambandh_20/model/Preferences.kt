package com.example.sambandh_20.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Preferences(val uid: String, val gender: Int?, val minAge: String, val maxAge: String, val regionOfOrigin: String, val religion: String,
                       val currentResidence: String, val expectedStay: Int?, val hobbies: String): Parcelable {
    constructor() : this("",null, "", "", "", "", "", null, "")
}