package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.FirebaseData
import com.example.project.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding // 뷰바인딩
    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater) // 바인딩 위해 선언
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener { // xml을 꾸며줘야 함
            val name = binding.etName.text.toString() // 먼저 변수를 toString으로 저장
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            signUpUser(name, email, password)
        }
    }

//    private fun signUpUser(name: String, email: String, password: String) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = mAuth.currentUser
//
//                    // FirebaseData 객체 생성
//                    val userData = FirebaseData(name, email, password)
//
//                    // Firestore에 데이터 추가
//                    addUserToFirestore(userData)
//
//                    // 회원가입 성공 시 수행할 작업
//                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
//                    // 예: 홈 화면으로 이동
//                } else {
//                    // 회원가입 실패 시 오류 처리
//                    // Toast.makeText(this, "회원가입 실패!", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, "회원가입 실패 : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    } // 아래 코드랑 같은 내용 but Logcat 출력하도록 되어있음
    private fun signUpUser(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 성공적으로 가입된 경우
                    val user = mAuth.currentUser
                    val userData = FirebaseData(name, email)

                    // Firebase Authentication에서 생성된 사용자의 UID를 가져옴 왜냐하면 user라는 컬렉션 안의 문서에 userID로 저장하기 위해서
                    val userId = user?.uid
                    // userID가 null이 아니어야지 Firestore에 데이터 추가하기
                    if(userId != null){
                        addUserToFirestore(userId, userData)
                    }

                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // 바로 로그인되지 않도록 회원가입 성공 후 로그아웃 -> 로그인 따로 하도록
                    mAuth.signOut()
                } else {

                    // 회원가입 실패 시 오류 처리
                    val errorMsg : String = if(task.exception is FirebaseAuthException) { // FirebaseAuthException 중 하나인지 확인하기
                        (task.exception as FirebaseAuthException).message ?: "회원가입 실패"
                    } else {
                        "FirebaseAuthException 외의 다른 이유로 회원가입 실패"
                    }
                    // Toast.makeText(this, "회원가입 실패 : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "회원가입 실패 : ${errorMsg}", Toast.LENGTH_SHORT).show()
                    Log.e("SignUpActivity", "회원가입 실패: ${task.exception}")
                }
            }
    }


    private fun addUserToFirestore(userId : String, userData: FirebaseData) {
        val userRef = db.collection("users")
        userRef.document(userId)// 사용자 UID를 문서 ID로 사용
            .set(userData)
            .addOnSuccessListener {
                // Firestore에 추가 성공 시 원하는 작업 수행
            }
            .addOnFailureListener { e ->
                // Firestore 추가 도중 오류 발생 시 처리
            }
    }
}
