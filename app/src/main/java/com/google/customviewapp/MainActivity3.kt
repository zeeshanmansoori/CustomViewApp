package com.google.customviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.AdapterView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.customviewapp.databinding.ActivityMain3Binding
import com.google.customviewapp.view3.BottomSheet
import com.google.customviewapp.view3.FragmentOne
import com.google.customviewapp.view3.FragmentTwo
import com.google.customviewapp.view3.PagerAdapter

class MainActivity3 : AppCompatActivity() {
    private val TAG = "zeeshan"
    private val binding by lazy {
        ActivityMain3Binding.inflate(layoutInflater)
    }

    private var oldSelection = 0


    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(binding.root)



        val adapter = PagerAdapter(
            listOf(FragmentOne(), FragmentTwo()),
            lifecycle = lifecycle,
            fragmentManager = supportFragmentManager
        )

        binding.viewPager2.adapter = adapter
        binding.viewPager2.isUserInputEnabled = false

        binding.btmNav.setOnItemSelectedListener(navigationItemSelectedListener)

    }


    // ViewPager Setup
    private val navigationItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        oldSelection = binding.viewPager2.currentItem
        when (item.itemId) {
            R.id.home -> {
                binding.viewPager2.currentItem = 0

            }
            R.id.music -> {
                binding.viewPager2.currentItem = 1
            }

            R.id.menu_menu -> {

                openBtmSheet()
            }


        }

        true
    }

    private fun openBtmSheet() {
        val sheet = BottomSheet()
        binding.btmNav.selectedItemId = getId(oldSelection)
        sheet.show(supportFragmentManager, "tag")
    }

    private fun getId(oldSelection: Int): Int {
        return when (oldSelection) {
            0 -> R.id.home
            1 -> R.id.music
            2 -> R.id.menu_menu
            else -> R.id.home
        }
    }


}