package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentEvent1Binding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class Event1Fragment : Fragment() {

    private lateinit var binding : FragmentEvent1Binding
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEvent1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val eventStringBuilder = StringBuilder()
                for (document in documents) {
                    val timestamp = document.getTimestamp("date")
                    val date = timestamp?.toDate()
                    // 날짜와 시간을 포맷하는 형식 지정
                    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시", Locale.getDefault())
                    val eventDate = dateFormat.format(date)

                    val eventName = document.getString("name")
                    val eventPlace = document.getString("place")

                    val sentence = "[${eventName}]이(가) ${eventDate}에 ${eventPlace}에서 열립니다."

                    eventStringBuilder.append(sentence).append("\n")

                }
                binding.tvDescription.text = eventStringBuilder.toString()
            }
            .addOnFailureListener{ exception ->

            }
    }
}