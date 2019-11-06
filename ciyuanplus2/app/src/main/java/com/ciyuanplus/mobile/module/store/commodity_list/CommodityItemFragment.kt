package com.ciyuanplus.mobile.module.store.commodity_list

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyFragment
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.R2.id.noDataView
import com.ciyuanplus.mobile.R2.id.rcl
import com.ciyuanplus.mobile.R2.layout.item
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.store.ShoppingMallFragment
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.module.webview.TbWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.RequstCommodityListParameter
import com.ciyuanplus.mobile.net.response.CommodityListItemRes
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import kotlinx.android.synthetic.main.fragment_commodityitem_list.*
import org.jetbrains.anko.support.v4.startActivity
import kotlin.math.log
/*
* 购物车商品页面
* */
class CommodityItemFragment : MyFragment() {

    private var categoryId: String? = ""
    private var searchValue: String? = null
    internal var isLoadMoreEnable = false
    internal var noMore = false
    private val pageSize = 20
    private var pageIndex = 0
    private var mAdapter: CommodityItemRecyclerViewAdapter? = null
    private val mList = ArrayList<CommodityListItemRes.CommodityItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            categoryId = it.getString(CATEGORY_ID, "")
            searchValue = it.getString(SEARCH_VALUE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_commodityitem_list, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rcl.layoutManager = GridLayoutManager(activity, 2)
        rcl.isNestedScrollingEnabled = true
        rcl.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = Utils.dip2px(10F)
                outRect.right = Utils.dip2px(10F)
            }
        })

        mAdapter = CommodityItemRecyclerViewAdapter(activity, mList)

        mAdapter?.setOnItemClickListener { adapter, view, position ->

            val item = adapter.getItem(position) as CommodityListItemRes.CommodityItem
            val userUuid = UserInfoData.getInstance().userInfoItem.uuid
            val sessionKey = SharedPreferencesManager.getString(
                    Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
            /* if (item.source==null){*/
                 val url = "${ApiContant.WEB_DETAIL_VIEW_URL}cyplus-share/prod.html?userUuid=$userUuid&authToken=$sessionKey&p=${item.prodId}"
                 startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to url, Constants.INTENT_TITLE_BAR_TITLE to "详情", Constants.INTENT_PAY_TOTAL_MONEY to -1)
            /* }else if(item.source=="taobao"){*/
              //   val url1 = "${ApiContant.WEB_DETAIL_VIEW_URL_TB}cyplus-share/prod.html?userUuid=$userUuid&authToken=$sessionKey&p=${item.prodId}"
                // startActivity<TbWebViewActivity>(Constants.INTENT_OPEN_URL to url1, Constants.INTENT_TITLE_BAR_TITLE to "详情", Constants.INTENT_PAY_TOTAL_MONEY to -1,Constants.TAO_BAO_LINK to item.taobaoLink,Constants.COUPON_LINK to item.couponLink)
            // }

             }
        rcl.adapter = mAdapter
        requestList()
    }

    private fun requestList() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COMMODITY_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequstCommodityListParameter(pageSize.toString(), pageIndex.toString(), categoryId.toString(), searchValue).requestBody)
        Log.d("id ",categoryId.toString())
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                val parentFragment = parentFragment as ShoppingMallFragment?
                parentFragment?.finishRefreshAndLoadMore()

                val response1 = CommodityListItemRes(s)

                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if (response1.communityListItem?.list != null) {

                        isLoadMoreEnable = response1.communityListItem?.list!!.size >= pageSize
                        noMore = response1.communityListItem?.list!!.size == 0

                        parentFragment?.setLoadMoreEnable(isLoadMoreEnable)
                        parentFragment?.setNoMoreData(noMore)
                        response1.communityListItem?.let { mList.addAll(it.list) }
                        mAdapter?.notifyDataSetChanged()
                        pageIndex++

                        if (mList.size == 0) {
                            rcl.visibility = View.GONE
                            noDataView.visibility = View.VISIBLE
                        } else {
                            rcl!!.visibility = View.VISIBLE
                            noDataView.visibility = View.GONE
                        }
                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                isLoadMoreEnable = true
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)

    }

    companion object {

        private const val CATEGORY_ID = "category-id"
        private const val SEARCH_VALUE = "search-value"

        @JvmStatic
        fun newInstance(categoryId: String, searchValue: String?) =
                CommodityItemFragment().apply {
                    arguments = Bundle().apply {
                        putString(CATEGORY_ID, categoryId)
                        putString(SEARCH_VALUE, searchValue)
                    }
                }

        val fragment = CommodityItemFragment()
    }

    public fun refresh() {
        pageIndex = 0
        mList.clear()
        requestList()
    }

    public fun loadMore() {
        requestList()
    }
}
