package com.ciyuanplus.mobile.module.home

import com.ciyuanplus.mobile.module.BaseContract
import com.ciyuanplus.mobile.net.bean.ActivityItem
import com.ciyuanplus.mobile.net.bean.BannerItem
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.bean.FriendsItem

/**
 * Created by Alen on 2017/12/11.
 */

class HomeFragmentContract {
    internal interface Presenter : BaseContract.Presenter {

        fun doRequest(reset: Boolean)

        fun requestBanner()

        fun requestHeadline()

         fun requestActivity()
        fun requestItemList()
        fun cancelLikePost(item: FreshNewItem)
        fun requestFollowUser(item: FreshNewItem)
        fun likePost(item: FreshNewItem)
    }

    interface View : BaseContract.View {

        fun stopRefreshAndLoad()

        fun stopRefreshAndRLoadMore(status: Boolean)

        fun setLoadMoreEnable(enable: Boolean)

        fun updateListView(postList: ArrayList<FreshNewItem>?)

        fun updateHeadLine(headline: ArrayList<FriendsItem>?)

        fun updateTopView(bannerList: ArrayList<BannerItem>?)

        fun showLoadingDialog()

        fun dismissLoadingDialog()

    }
}
