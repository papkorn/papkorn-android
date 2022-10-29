package com.nuller.popkorn.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabsAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragmentsList = arrayListOf<Fragment>()
    private var titlesList = arrayListOf<String>()

    override fun getCount(): Int {
        return fragmentsList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentsList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titlesList[position]
    }

    fun addTab(title: String, fragment: Fragment) {
        titlesList.add(title)
        fragmentsList.add(fragment)
    }

    fun removeTab(position: Int) {
        if (!fm.isDestroyed)
            fm.beginTransaction().remove(fragmentsList[0]).commit()
        fragmentsList.removeAt(position)
        titlesList.removeAt(position)
        notifyDataSetChanged()
    }

    fun clearTabs() {
        for (fragment in fragmentsList) {
            if (!fm.isDestroyed)
                fm.beginTransaction().remove(fragment).commit()
        }
        fragmentsList.clear()
        titlesList.clear()
        notifyDataSetChanged()
    }
}