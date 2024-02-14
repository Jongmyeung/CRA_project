package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class Event1Fragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val timestamp = document.getTimestamp("date")
                    val date = timestamp?.toDate()
                    // 날짜와 시간을 포맷하는 형식 지정
                    val dateFormat = SimpleDateFormat("yyyy")
                }
            }
    }
}