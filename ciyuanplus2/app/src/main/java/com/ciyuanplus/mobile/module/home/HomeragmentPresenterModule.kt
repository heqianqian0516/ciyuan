package com.ciyuanplus.mobile.module.home

import dagger.Module
import dagger.Provides

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/2/28
 * class  : HomeragmentPresenterModule.kt
 * desc   : HomeFragmentPresenterModule
 * version: 1.0
 */


@Module
class HomeFragmentPresenterModule(private val mView: HomeFragmentContract.View) {

    @Provides
    internal fun providesHomeFragmentContractView(): HomeFragmentContract.View {
        return mView
    }
}
