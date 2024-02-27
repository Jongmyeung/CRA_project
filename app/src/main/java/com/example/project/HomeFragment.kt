package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    // Fragment의 lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 7일 이내의 이벤트가 있는지 확인하는 로직
        firestore.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val currentDate = Calendar.getInstance().time
                val sevenDaysLater = Calendar.getInstance()
                sevenDaysLater.add(Calendar.DAY_OF_YEAR, 7)

                var hasEventWithin7Days = false

                for (document in documents) {
                    val timestamp = document.getTimestamp("date")
                    val eventDate = timestamp?.toDate()

                    if (eventDate != null && eventDate.after(currentDate) && eventDate.before(sevenDaysLater.time)) {
                        // 7일 이내에 이벤트가 있는 경우
                        hasEventWithin7Days = true
                        break
                    }
                }

                // 7일 이내에 이벤트가 있는 경우 버튼을 보이도록 설정 && 버튼 눌렀을 때 이동하도록 설정
                if (hasEventWithin7Days) {
                    binding.btnToApply.visibility = View.VISIBLE

                    binding.btnToApply.setOnClickListener {
                        try {
                            // 신청 버튼을 클릭하면 Event1Fragment로 이동
                            // findNavController().navigate(R.id.action_homeFragment_to_event1Fragment)

                            // FragmentManager 사용하기
                            // Fragment Transaction 시작
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            // 이동할 Fragment를 추가하거나 교체
                            transaction.replace(R.id.frame_container, Event1Fragment())
                            // 백스택에 추가하여 이전 Fragment로 돌아갈 수 있도록 함
                            transaction.addToBackStack(null)
                            // Transaction 완료
                            transaction.commit()
                        } catch (e: IllegalArgumentException) {
                            // 목적지를 찾을 수 없는 경우 에러 메시지를 로그로 출력
                            Log.e("NavigationError", "Navigation destination not found: ${e.message}")
                            // 또는 필요에 따라 사용자에게 알림을 표시하거나 다른 작업을 수행할 수 있습니다.
                        }
                    }

                    firestore.collection("events")
                        .get()
                        .addOnSuccessListener { documents ->
                            val eventStringBuilder = StringBuilder()
                            for(document in documents) {
                                val eventName = document.getString("name")

                                val sentence = "[${eventName}]"

                                eventStringBuilder.append(sentence).append("\n")
                            }
                            binding.eventForBegin.text = eventStringBuilder.toString()
                        }
                        .addOnSuccessListener { exception ->

                        }

                    binding.tv1SignHome.text = "곧 진행돼요."
                    
                } else {
                    binding.btnToApply.visibility = View.GONE

                    binding.eventForBegin.text = "곧 진행되는 이벤트는 없습니다."
                }
            }
            .addOnFailureListener { exception ->
                // 오류 처리
            }
    }
}
