package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.project.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // setContentView(R.layout.activity_main) & setContentView(binding.root)의 차이는 data binding, 관리의 차이

//        val splashIntent = Intent(this, LoadingActivity::class.java) // 스플래쉬 스크린인 loadingActivity 실행
//        startActivity(splashIntent)

//        binding.applyBtn.setOnClickListener{
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent) // 화면 전환
//        }

        if (intent.getBooleanExtra("replaceFragment", false)) {
            // replaceFragment 플래그가 true인 경우 ProfileFragment로 교체
            replaceWithProfileFragment()
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){ // when은 else가 필수적으로 필요한가?
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_checkButton -> {
                    replaceFragment(RoadMapFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    // 만약 회원가입 실패하면 ProfuleFragment로 교체해주기
    private fun replaceWithProfileFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, ProfileFragment())
        fragmentTransaction.commit()
    }
}