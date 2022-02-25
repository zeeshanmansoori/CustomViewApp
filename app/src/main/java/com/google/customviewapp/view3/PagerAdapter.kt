package com.google.customviewapp.view3

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(
    private val list: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = list.size


    override fun createFragment(position: Int): Fragment {
        return list[position]
    }


    fun getFragment(position: Int): Fragment {

        return list[position]

    }


}