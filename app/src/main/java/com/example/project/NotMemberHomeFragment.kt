package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentNotMemberHomeBinding


class NotMemberHomeFragment : Fragment() {

    private lateinit var binding: FragmentNotMemberHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotMemberHomeBinding.inflate(inflater, container, false)

        binding.btnForSignUpFromNotSignHome1.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnForSignUpFromNotSignHome2.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }
}