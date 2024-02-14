package com.example.project

import com.google.firebase.Timestamp

data class FirebaseDataForEvent(
    val eventName : String?,
    val personnel : Int?,
    val eventDate : Timestamp?,
    val eventPlace : String?
)
