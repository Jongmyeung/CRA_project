package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileFragment : Fragment() {

    private lateinit var binding : FragmentUserProfileBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()

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

                        val sentenceName = "이름: ${userName ?: ""}"
                        val sentenceEmail = "이메일: ${userEmail ?: ""}"
                        val sentenceNickName = "닉네임: ${userNickName ?: ""}"
                        val sentenceGithub = "Github: ${userGithub ?: ""}"
                        val sentenceStudentNumber = "학번: ${userStudentNumber ?: ""}"
                        val sentenceUserProfile = "[${userName ?: ""}]님의 프로필이에요"

                        binding.tvName.text = sentenceName
                        binding.tvEmail.text = sentenceEmail
                        binding.tvNickName.text = sentenceNickName
                        binding.tvStudentNumber.text = sentenceStudentNumber
                        binding.tvUserProfile1.text = sentenceUserProfile
                        binding.tvGithubAddress.text = sentenceGithub
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패 시 처리
                }
        }
    }
}
