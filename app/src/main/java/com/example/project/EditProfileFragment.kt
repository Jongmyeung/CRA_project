package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

                        binding.btnEdit.setOnClickListener {
                            val updatedFields = mutableMapOf<String, Any>()

                            // EditText에 입력된 내용 가져오기
                            val updatedName = binding.etName.text.toString()
                            val updatedNickName = binding.etNickName.text.toString()
                            val updatedEmail = binding.etEmail.text.toString()
                            val updatedGithub = binding.etGithub.text.toString()
                            val updatedStudentNumberText = binding.etStudentNumber.text.toString()
                            val updatedStudentNumber: Long? = updatedStudentNumberText.toLongOrNull()

                            if (updatedStudentNumber != null) {
                                // StudentNumber가 숫자로 변환 가능한 경우에만 업데이트
                                updatedFields["studentNumber"] = updatedStudentNumber
                            } else {
                                // 숫자로 변환할 수 없는 경우에 대한 처리
                                Toast.makeText(requireContext(), "숫자를 입력해주세요", Toast.LENGTH_SHORT).show()
                            }
                            if (updatedName.isNotEmpty()) {
                                updatedFields["name"] = updatedName
                            }
                            if (updatedNickName.isNotEmpty()) {
                                updatedFields["nickName"] = updatedNickName
                            }
                            // Firestore에 업데이트할 필드 설정
                            if (updatedEmail.isNotEmpty()) {
                                updatedFields["email"] = updatedEmail
                            }
                            if (updatedGithub.isNotEmpty()) {
                                updatedFields["Github"] = updatedGithub
                            }

                            // Firestore에 업데이트
                            if (updatedFields.isNotEmpty()) {
                                firestore.collection("users")
                                    .document(userId)
                                    .update(updatedFields)
                                    .addOnSuccessListener {
                                        try {
                                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                            transaction.replace(R.id.frame_container, UserProfileFragment())
                                            transaction.addToBackStack(null)
                                            transaction.commit()
                                        } catch (e : IllegalArgumentException) {
                                            Toast.makeText(requireContext(), "입력하신 내용으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                                            Log.e("NavigationError", "Navigation destination not found: ${e.message}")
                                        }
                                    }
                                    .addOnFailureListener { exception ->

                                    }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패 시 처리
                }
        }
    }
}
