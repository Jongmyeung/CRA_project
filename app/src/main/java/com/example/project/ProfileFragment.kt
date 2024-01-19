package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        binding.btnToSignUpTest.setOnClickListener{
            val intent = Intent(requireContext(), SignUpActivity::class.java) // requireContext() 이해...필요
            startActivity(intent)
        }

        binding.btnToLoginTest.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnToSec.setOnClickListener {
            val intent = Intent(requireContext(), SecessionActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}