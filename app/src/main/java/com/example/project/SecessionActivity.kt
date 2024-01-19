package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.project.databinding.ActivitySecessionBinding
import com.google.firebase.auth.FirebaseAuth

class SecessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecessionBinding
    private lateinit var auth : FirebaseAuth
    private val TAG = "SecessionActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() // auth 초기화

        binding.btnSec.setOnClickListener {
            deleteUser()
        }
    }

    private fun deleteUser() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete() // Firebase Authentication에서 사용자 계정을 삭제하는 것 -> 아직 데이터 삭제는 안됨
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                    // 사용자 계정이 성공적으로 삭제된 경우 추가 작업 수행
                    // 로그아웃 처리
                    auth.signOut()

                    Toast.makeText(this, "탈퇴 성공!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "Failed to delete user account.", task.exception)

                    Toast.makeText(this, "탈퇴 실패 : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}