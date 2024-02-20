package com.example.project

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.project.FirebaseData
import com.example.project.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
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

        binding.etEmail.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cursor))

        binding.etPassword.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cursor))

        binding.etPasswordForCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cursor))

        binding.btnSignUp.setOnClickListener { // xml을 꾸며줘야 함
            val email = binding.etEmail.text.toString() // 먼저 변수를 toString으로 저장
            val password = binding.etPassword.text.toString()
            val passwordForCheck = binding.etPasswordForCheck.text.toString()

            // 이걸 버튼 눌렀을 때?
            if(password == passwordForCheck){
                signUpUser(email, password)
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnToLoginFromSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnEyePassword.setOnClickListener {

            if(binding.etPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // 현재 비밀번호 입력 타입이 textPassword인 경우, 보이게 변경
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()

                binding.btnEyePassword.setImageResource(R.drawable.icon_eye_open_alt)
            } else {
                // 현재 비밀번호 입력 타입이 보이는 상태인 경우, 다시 textPassword로 변경
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                // 눈 모양 아이콘을 숨기는 상태로 변경
                binding.btnEyePassword.setImageResource(R.drawable.icon_eye_alt_) // 비밀번호 보이기 아이콘 이미지
            }
        }

        binding.btnEyePasswordCheck.setOnClickListener {

            if(binding.etPasswordForCheck.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // 현재 비밀번호 입력 타입이 textPassword인 경우, 보이게 변경
                binding.etPasswordForCheck.transformationMethod = HideReturnsTransformationMethod.getInstance()

                binding.btnEyePasswordCheck.setImageResource(R.drawable.icon_eye_open_alt)
            } else {
                // 현재 비밀번호 입력 타입이 보이는 상태인 경우, 다시 textPassword로 변경
                binding.etPasswordForCheck.transformationMethod = PasswordTransformationMethod.getInstance()
                // 눈 모양 아이콘을 숨기는 상태로 변경
                binding.btnEyePasswordCheck.setImageResource(R.drawable.icon_eye_alt_) // 비밀번호 보이기 아이콘 이미지
            }
        }
    }
    // edittext가 달라졌기에 수정해야함.
    private fun signUpUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 성공적으로 가입된 경우

//                    회원가입 성공 시 SharedPreferences에 회원가입 여부 저장
//                    SharedPreferencse는 데이터를 저장할 때 사용하는 방법 중 하나 처음에 회원가입 유무 따지기 위해서 추가한 코드
//
//                    val sharedPref = this@SignUpActivity.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
//                    val editor = sharedPref.edit()
//                    editor.putBoolean("is_member", true)
//                    editor.apply()

                    // Firebase는 login 유무를 자동으로 기억해주기에 그 코드 사용하자
                    val user: FirebaseUser? = mAuth.currentUser
                    val userData = FirebaseData(email)

                    // Firebase Authentication에서 생성된 사용자의 UID를 가져옴 왜냐하면 user라는 컬렉션 안의 문서에 userID로 저장하기 위해서
                    val userId = user?.uid
                    // userID가 null이 아니어야지 Firestore에 데이터 추가하기
                    if(userId != null){
                        addUserToFirestore(userId, userData)
                        sendEmailVerification()
                    }
                    // 아래의 함수에서 이메일 인증
//                    checkEmailVerification()
                    // Firebase는 회원가입 후에 이메일 링크 보내는걸 지원하기에 먼저 회원가입 하고 이메일 링크 보내는걸로
//                    Toast.makeText(this, "입력하신 이메일로 확인 링크를 전송했습니다.", Toast.LENGTH_LONG).show()
//                    sendEmailVerification(name, email, password)
//
//                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)

                    // 바로 로그인되지 않도록 회원가입 성공 후 로그아웃 -> 로그인 따로 하도록
//                    mAuth.signOut()
                } else {


                    // 회원가입 실패 시 오류 처리
                    val errorMsg : String = if(task.exception is FirebaseAuthException) { // FirebaseAuthException 중 하나인지 확인하기
                        (task.exception as FirebaseAuthException).message ?: "회원가입 실패"
                    } else {
                        "FirebaseAuthException 외의 다른 이유로 회원가입 실패"
                    }
                    // Toast.makeText(this, "회원가입 실패 : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "회원가입 실패 : ${errorMsg}", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    Log.e("SignUpActivity", "회원가입 실패: ${task.exception}")
                }
            }
    }

//    private fun checkEmailVerification() {
//        val user: FirebaseUser? = mAuth.currentUser
//        if (user != null) {
//            user.reload()
//                .addOnCompleteListener { reloadTask ->
//                    if (reloadTask.isSuccessful) {
//                        if (user.isEmailVerified) {
//                            // 이메일이 인증되었음
//
//                            Toast.makeText(this, "이메일이 성공적으로 인증되었습니다.", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)
//
//                            mAuth.signOut() // 회원가입 성공 후 로그아웃
//                            finish()
//                        } else {
//                            // 이메일이 아직 인증되지 않음
//                            Toast.makeText(this, "이메일이 인증되지 않았습니다.", Toast.LENGTH_SHORT).show()
//                            // 사용자에게 알림 또는 재전송 옵션을 제공할 수 있습니다.
//                        }
//                    } else {
//                        Toast.makeText(this, "사용자 정보 리로드 실패", Toast.LENGTH_SHORT).show()
//                        Log.e("SignUpActivity", "사용자 정보 리로드 실패: ${reloadTask.exception}")
//                    }
//                }
//        }
//    }

    private fun sendEmailVerification() {
        val user: FirebaseUser? = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    updateUI(user)
                } else {
                    Toast.makeText(this, "이메일 확인 링크 전송 실패", Toast.LENGTH_SHORT).show()
                    Log.e("SignUpActivity", "이메일 확인 링크 전송 실패: ${verificationTask.exception}")
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user!= null) {
            Toast.makeText(this, "이메일 확인 링크가 전송되었습니다. 확인 후 로그인하세요.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
