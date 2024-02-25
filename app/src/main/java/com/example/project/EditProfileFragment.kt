package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentEditProfileBinding
import com.google.firebase.firestore.FirebaseFirestore


class EditProfileFragment : Fragment() {

    private lateinit var binding : FragmentEditProfileBinding
    private val firestore = FirebaseFirestore.getInstance()

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

        firestore.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                val eventStringBuilderName = StringBuilder()
                val eventStringBuilderEmail = StringBuilder()
                val eventStringBuilderNickName = StringBuilder()
                val eventStringBuilderGithub = StringBuilder()
                val eventStringBuilderStudentNumber = StringBuilder()
                val eventStringBuilderUserProfile = StringBuilder()

                for(document in documents) {
                    val userName = document.getString("name")
                    val userNickName = document.getString("nickName")
                    val userEmail = document.getString("email")
                    val userGithub = document.getString("Github")
                    val userStudentNumber = document.getLong("studentNumber")

                    val sentenceName = "이름: ${userName}"
                    val sentenceEmail = "이메일: ${userEmail}"
                    val sentenceNickName = "닉네임: ${userNickName}"
                    val sentenceGithub = "Github: ${userGithub}"
                    val sentenceStudentNumber = "학번: ${userStudentNumber}"
                    val sentenceUserProfile = "[${userName}]님의 프로필이에요"

                    eventStringBuilderName.append(sentenceName).append("\n")
                    eventStringBuilderEmail.append(sentenceEmail).append("\n")
                    eventStringBuilderNickName.append(sentenceNickName).append("\n")
                    eventStringBuilderGithub.append(sentenceGithub).append("\n")
                    eventStringBuilderStudentNumber.append(sentenceStudentNumber).append("\n")
                    eventStringBuilderUserProfile.append(sentenceUserProfile).append("\n")
                }
                binding.etName.hint = eventStringBuilderName.toString()
                binding.tvEmail.text = eventStringBuilderEmail.toString()
                binding.tvNickName.text = eventStringBuilderNickName.toString()
                binding.tvStudentNumber.text = eventStringBuilderStudentNumber.toString()
                binding.tvUserProfile1.text = eventStringBuilderUserProfile.toString()
                binding.tvGithubAddress.text = eventStringBuilderGithub.toString()
            }
            .addOnFailureListener {exception ->

            }
    }

}