package com.ciyuanplus.mobile.module.settings.address

import dagger.Component


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = [AddressManagerPresenterModule::class])
interface AddressManagerPresenterComponent {
    fun inject(mActivity: AddressManagerActivity)
}
