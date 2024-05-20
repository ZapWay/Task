package com.example.task

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.task.fragments.BookmarksFragment
import com.example.task.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fragmentManager: FragmentManager

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    private fun initBottomNavigationViewListener(){
        bottomNavigation.setOnItemSelectedListener { item->
            when (item.itemId){
                R.id.home_button ->{
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bookmarks_button->{
                    replaceFragment(BookmarksFragment())
                    true
                }
                else ->{
                    false
                }
            }
        }
    }


    private fun initGUI(){
        bottomNavigation = findViewById(R.id.bottomNavigationView)
        bottomNavigation.itemIconTintList = null
        replaceFragment(HomeFragment())
    }

    private fun initListeners(){
        initBottomNavigationViewListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initGUI()
        initListeners()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}