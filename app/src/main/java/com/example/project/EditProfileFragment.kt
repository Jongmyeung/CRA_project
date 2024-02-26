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
                        val sentenceUserProfile = "[${userName ?: ""}]님의 프로필이에요"

                        // EditText의 hint로 사용자의 이름 설정
                        binding.etName.hint = userName
                        binding.etEmail.hint = userEmail
                        binding.etNickName.hint = userNickName
                        binding.etStudentNumber.hint = userStudentNumber.toString()
                        binding.etGithub.hint = userGithub
                        binding.tvUserProfile1.text = sentenceUserProfile
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패 시 처리
                }
        }
    }
}
