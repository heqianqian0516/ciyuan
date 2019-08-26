package com.ciyuanplus.mobile.module.store.shopmall


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyFragment
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.loader.GlideImageLoader
import com.ciyuanplus.mobile.module.store.commodity_list.CommodityItemFragment
import com.ciyuanplus.mobile.module.store.commodity_list.CommodityItemFragmentNew
import com.ciyuanplus.mobile.module.store.shop_car.ShopCarActivity
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.BannerItem
import com.ciyuanplus.mobile.net.parameter.RequestBannerApiParameter
import com.ciyuanplus.mobile.net.parameter.SaveDeviceTokenParameter
import com.ciyuanplus.mobile.net.response.CommodityTypeListItem
import com.ciyuanplus.mobile.net.response.RequestBannerListResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.flyco.tablayout.listener.OnTabSelectListener
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_shopping_mall_new.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ShoppingMallFragmentNew : MyFragment() {

    private var isLoadMoreEnable = false
    private val pageSize = 20
    private var pageIndex = 0
    private val mFragments = ArrayList<CommodityItemFragmentNew>()
    open val mTitles = ArrayList<String>()
    private lateinit var mAdapter: MyPagerAdapter
    private var mSelectTab = 0
    private val mTopList = ArrayList<BannerItem>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_mall_new, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = MyPagerAdapter(childFragmentManager)
        vPShopMallNew.adapter = mAdapter

        vPShopMallNew.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                vPShopMallNew.resetHeight(position)
            }
        })


        tab.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {

                mSelectTab = position
                isLoadMoreEnable = mFragments[position].isLoadMoreEnable
                smartRefreshLayout.setEnableLoadMore(isLoadMoreEnable)
            }

            override fun onTabReselect(position: Int) {

            }
        })

        smartRefreshLayout.setOnRefreshListener {
            requestBanner()

            val fragment =  mAdapter.getItem(mSelectTab) as CommodityItemFragmentNew
            fragment.refresh()
        }

        smartRefreshLayout.setOnLoadMoreListener {
            val fragment = mFragments[mSelectTab] as CommodityItemFragment
            if (fragment.isLoadMoreEnable) {
                fragment.loadMore()

            }
        }
        shopCarLayout.setOnClickListener { startActivity<ShopCarActivity>() }
        doRequest(true)

    }

    private fun doRequest(reset: Boolean) {
        if (reset) {

            pageIndex = 0
            requestBanner()
            requestTypes()

        }
    }


    private fun requestBanner() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_BANNER_LIST_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestBannerApiParameter("3").requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                val response1 = RequestBannerListResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    mTopList.clear()
                    mTopList.addAll(response1.bannerListItem.list)
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
                updateTopView(mTopList)

            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                updateTopView(mTopList)

            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }


    fun updateTopView(bannerList: ArrayList<BannerItem>) {

        val images = ArrayList<String>()
        for (item in bannerList) {

            images.add(Constants.IMAGE_LOAD_HEADER + item.img)
        }

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
//        if (loader == null) {
//            loader = GlideImageLoader()
//        }

        banner.setImageLoader(GlideImageLoader())

        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage)
//        设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles)
//        设置自动轮播，默认为true
        this.banner.isAutoPlay(true)
//        设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.setOnBannerListener {

            clickBanner(bannerList.get(it))
        }
        //设置图片集合
        banner.setImages(images)
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }

    private fun clickBanner(banner: BannerItem) {

        when (banner.type) {
            0 -> {
            }
            1 -> {
                startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to banner.param)
            }
        }
    }


    private fun requestTypes() {

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COMMODITY_TYPE)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(SaveDeviceTokenParameter("").requestBody)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                val response1 = CommodityTypeListItem(s)

                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    mTitles.clear()
                    mFragments.clear()

                    for (i in 0 until response1.items.size) {

                        response1.items[i].name?.let { mTitles.add(response1.items[i].name!!) }
                        val fragment = CommodityItemFragmentNew.newInstance(response1.items[i].id, "", vPShopMallNew, i)

                        mFragments.add(fragment)
                    }

                    isLoadMoreEnable = response1.items.size >= pageSize
                    tab.setViewPager(vPShopMallNew)
                    mAdapter.notifyDataSetChanged()
                } else {
                    isLoadMoreEnable = false
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
                smartRefreshLayout.setEnableLoadMore(isLoadMoreEnable)
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.finishRefresh()
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                isLoadMoreEnable = false
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.finishRefresh()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }


    inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {

            return mFragments[position]
        }

        override fun getCount(): Int = if (mTitles.size >= mFragments.size) {
            mFragments.size
        } else {
            mTitles.size
        }


        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles[position]
        }
    }

    public fun finishRefreshAndLoadMore() {
        smartRefreshLayout.finishLoadMore()
        smartRefreshLayout.finishRefresh()
    }

}
