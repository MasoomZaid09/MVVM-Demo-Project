package com.kanabix.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class viewPagerAdapter(supportFragmentManager: FragmentManager): FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // creating lists to store fragment and there titles
    val mFragmentList = ArrayList<Fragment>()
    val mFragmentTitleList = ArrayList<String>()

    // method to get count of fragments
    override fun getCount(): Int {
        return mFragmentList.size
    }

    // method to set fragments on correct positions
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    // set title on corresponding fragments
    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    // creating funtion to store fragments and there titles
    fun getFragment(fragment: Fragment, title : String){

        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}
