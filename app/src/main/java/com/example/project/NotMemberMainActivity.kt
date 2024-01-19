package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotMemberMainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_member_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){ // when은 else가 필수적으로 필요한가?
                R.id.bottom_home -> {
                    replaceFragment(NotMemberHomeFragment())
                    true
                }
                R.id.bottom_checkButton -> {
                    replaceFragment(NotMemberRoadMapFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replaceFragment(NotMemberProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(NotMemberHomeFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

}