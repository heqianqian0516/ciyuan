package com.ciyuanplus.mobile.module.settings.address

import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.module.BaseContract

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Alen on 2017/12/11.
 */

class AddressManagerContract {
    internal interface Presenter : BaseContract.Presenter {
        fun initData()
        fun getAddressList()
        fun handleEvent(eventMessage: EventCenterManager.EventMessage)
        fun removeAddress(position: Int)
    }

    interface View : BaseContract.View {
        fun getRecyclerView(): RecyclerView?
        fun showContent()
        fun showEmptyView()
    }
}
