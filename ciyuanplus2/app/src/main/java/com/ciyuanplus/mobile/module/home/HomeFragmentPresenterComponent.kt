package com.ciyuanplus.mobile.module.home

import dagger.Component


/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/2/28
 * class  : HomeFragmentPresenterComponent.kt
 * desc   : HomeFragmentPresenterComponent
 * version: 1.0
 */


@Component(modules = [HomeFragmentPresenterModule::class])
interface HomeFragmentPresenterComponent {
    fun inject(mFragment: HomeFragment)
}
