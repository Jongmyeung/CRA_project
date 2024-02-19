package com.example.project

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.project.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth // FirebaseAuth 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화
        auth = Firebase.auth // auth = FirebaseAuth.getInstance()랑 같은 하지만 코틀린스러운 표현

        binding.etEmailLogin.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cursor))

        binding.etPasswordLogin.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cursor))

        binding.btnLogin.setOnClickListener{
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()

            signIn(email, password) // Login과 Signin은 같다
        }

        binding.btnEyeIcon.setOnClickListener {

            if(binding.etPasswordLogin.transformationMethod == PasswordTransformationMethod.getInstance()) {

                binding.etPasswordLogin.transformationMethod = HideReturnsTransformationMethod.getInstance()

                binding.btnEyeIcon.setImageResource(R.drawable.icon_eye_open_alt)
            } else {

                binding.etPasswordLogin.transformationMethod = PasswordTransformationMethod.getInstance()

                binding.btnEyeIcon.setImageResource(R.drawable.icon_eye_alt_)
            }
        }

        binding.btnToSignUpFromLogin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnToPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    fun signIn(email : String, password : String){
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    // 로그인 성공한 경우
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "로그인 실패 : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



}