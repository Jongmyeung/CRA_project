package com.example.project

data class FirebaseData( // 회원가입 정보 받을 때의 정보들
    val Name : String?,
    val Email : String?
    // Firestore에 넣을 때는 비밀번호 넣을 필요 없음
    // 그렇기에 해시화할 필요 없음
    // val Password : String
)
