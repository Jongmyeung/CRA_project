package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.project.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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

        binding.btnToApply.setOnClickListener {
            try {
                // 신청 버튼을 클릭하면 Event1Fragment로 이동
                findNavController().navigate(R.id.action_homeFragment_to_event1Fragment)
            } catch (e: IllegalArgumentException) {
                // 목적지를 찾을 수 없는 경우 에러 메시지를 로그로 출력
                Log.e("NavigationError", "Navigation destination not found: ${e.message}")
                // 또는 필요에 따라 사용자에게 알림을 표시하거나 다른 작업을 수행할 수 있습니다.
            }
        }
    }
}
