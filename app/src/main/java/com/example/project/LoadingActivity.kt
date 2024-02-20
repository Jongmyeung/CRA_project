package com.example.project

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.View
import com.example.project.databinding.ActivityLoadingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoadingBinding
    private var mAuth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth!!.currentUser
        val intent = updateUI(currentUser)
//        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
//        val isMember = sharedPref.getBoolean("is_member", false) // 만약 값이 없으면 false 반환

//         val intent = Intent(this, MainActivity::class.java) // 회원가입 한 경우
//         val intent2 = Intent(this, MainActivity::class.java) // 회원가입 안 한 경우
//        val intent = if (isMember) {
//            Intent(this, MainActivity::class.java)
//        } else {
//            Intent(this, NotMemberMainActivity::class.java)
//        }
        // 경우에 따라 화면 이동이 달라짐
        binding.root.postDelayed({
            if(!isFinishing){
                startActivity(intent)
                finish()
            }
        }, 3000)
    }

    private fun updateUI(currentUser: FirebaseUser?): Intent {
        return if (currentUser == null) {
            Intent(this, NotMemberMainActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
    }

    override fun onPause() {
        super.onPause()
        // 스플래쉬 화면에서 벗어나면 핸들러에 대기 중인 작업을 취소하여 메모리 누수를 방지합니다.
        // 만약에 이동하기 전에 뒤로 가기 버튼을 누른다면, 스플래쉬 화면으로 돌아가지 않게 됩니다.
        finish()
    }
}