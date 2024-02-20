package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResetPasswordBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.btnToPasswordReset.setOnClickListener {

            val etEmail = binding.etEmail.text.toString()
            val etEmailCheck = binding.etEmailCheck.text.toString()
            // 조건문
            if(etEmail == etEmailCheck){
                val intent = Intent(this, ActivityResetPasswordBinding::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "이메일이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}