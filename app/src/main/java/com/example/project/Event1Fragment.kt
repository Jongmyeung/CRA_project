package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.project.databinding.FragmentEvent1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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

                    // UTF-8과 firestore에서 사용하는? UTC+9랑 3시간 차이가 남(이거는 내 계산에만 의존)
                    val sentence = "[${eventName}]이(가)\n ${eventDate}에\n ${eventPlace}에서 열립니다."

                    eventStringBuilder.append(sentence).append("\n")

                }
                binding.tvDescription.text = eventStringBuilder.toString()
            }
            .addOnFailureListener{ exception ->

            }

        binding.btnToApplyEvent.setOnClickListener {
            // Firebase Authentication에서 현재 로그인한 사용자 가져오기
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) { // 로그인된 상태인지 먼저 확인하기
                val userId = user.uid

                firestore.collection("events")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {

                            // firebase에서는 숫자 타입이지만 문서에서 해당 필드가 문자열로 저장되어 있을 수 있음.
                            // val currentPersonnel = document.getLong("personnel") ?: 0

                            // Firestore에서 가져온 각 문서의 고유한 ID(event별로 다름)
                            val eventId = document.id
                            val eventCapacity = document.getLong("capacity") ?: 0
                            val currentPersonnel = document.getLong("personnel") ?: 0

                            // if문 안에서 선언하면 그 안에서만 사용 가능 따라서 밖에서 선언해야 함.
                            val updatedPersonnel = currentPersonnel // 선언 및 초기화

                            if (eventCapacity > currentPersonnel) { // 수용 인원 넘는지 확인
                                val eventParticipants =
                                    document.get("participants") as? List<String> ?: emptyList()

                                if (!eventParticipants.contains(userId)) { // 중복 신청인지 확인
                                    val updatedPersonnel = currentPersonnel + 1

                                    // eventParticipants.add(userId)

                                    document.reference
                                        .update(
                                            "personnel", updatedPersonnel,
                                            "participants", FieldValue.arrayUnion(userId) // eventParticipants
                                        )
                                        .addOnSuccessListener {
                                            Toast.makeText(requireContext(), "신청되었습니다.\n감사합니다.", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {e ->
                                            Toast.makeText(requireContext(), "업데이트를 처리하는 동안 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "이미 신청한 사용자입니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                // Fragment에서는 this 대신에 requireContext()
                                Toast.makeText(
                                    requireContext(),
                                    "정원이 다 차 신청하실 수 없습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
//                            중복되는 부분
//                            val eventRef = firestore.collection("events").document(eventId)
//                            eventRef
//                                .update("personnel", updatedPersonnel)
//                                .addOnSuccessListener {
//                                    Toast.makeText(
//                                        requireContext(),"추가되었습니다.\n감사합니다.", Toast.LENGTH_SHORT).show()
//                                }
//                                .addOnFailureListener { e ->
//                                    Toast.makeText(
//                                        requireContext(),"업데이트를 처리하는 동안 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
//                                }
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "먼저 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
    }

}