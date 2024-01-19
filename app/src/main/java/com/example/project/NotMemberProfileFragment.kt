package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.ActivitySignUpBinding
import com.example.project.databinding.FragmentNotMemberProfileBinding


class NotMemberProfileFragment : Fragment() {

    private lateinit var binding : FragmentNotMemberProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotMemberProfileBinding.inflate(inflater)

        binding.btnToSignUp.setOnClickListener{
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}