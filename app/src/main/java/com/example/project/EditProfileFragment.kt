package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileFragment : Fragment() {

    private lateinit var binding : FragmentEditProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 사용자 가져오기
        val currentUser = auth.currentUser

        // 현재 사용자가 로그인되어 있는지 확인
        if (currentUser != null) {
            val userId = currentUser.uid

            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userName = document.getString("name")
                        val userNickName = document.getString("nickName")
                        val userEmail = document.getString("email")
                        val userGithub = document.getString("Github")
                        val userStudentNumber = document.getLong("studentNumber")

                        // EditText의 hint로 사용자의 이름 설정
                        binding.etName.hint = userName

                        // 나머지 데이터는 TextView에 표시
                        binding.tvEmail.text = userEmail
                        binding.tvNickName.text = userNickName
                        binding.tvStudentNumber.text = userStudentNumber.toString()
                        binding.tvGithubAddress.text = userGithub
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패 시 처리
                }
        }
    }
}
