package com.ciyuanplus.mobile.module.settings.address

import dagger.Module
import dagger.Provides

/**
 * Created by Alen on 2017/12/11.
 */
@Module
internal class AddressManagerPresenterModule(private val mView: AddressManagerContract.View) {

    @Provides
    fun providesAddressManagerContractView(): AddressManagerContract.View {
        return mView
    }
}
