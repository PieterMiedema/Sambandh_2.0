package com.example.sambandh_20.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String, val profileImageUrL: String, val displayName: String,
                val dateOfBirth: String, val regionOfOrigin: String, val religion: String,
                val currentResidence: String, val expectedStay: Int?, val hobbies: String,
                val biography: String): Parcelable {
    constructor() : this("","","",  "", "", "", "", null, "", "")
}