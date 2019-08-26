package com.ciyuanplus.mobile.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TitleFragmentPagesAdapter(var fmList:List<Fragment>, fm: FragmentManager?):
        FragmentStatePagerAdapter(fm!!){

    override fun getItem(p0: Int) = fmList[p0]

    override fun getCount() = fmList.size
}