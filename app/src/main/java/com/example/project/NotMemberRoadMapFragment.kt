package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentNotMemberRoadMapBinding


class NotMemberRoadMapFragment : Fragment() {

    private lateinit var binding: FragmentNotMemberRoadMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotMemberRoadMapBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root

        binding.btnForSignUpFromNotSignRoadmap1.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnForSignUpFromNotSignRoadmap2.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}