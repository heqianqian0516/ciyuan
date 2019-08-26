package com.ciyuanplus.mobile.module.store.commodity_list

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.store.shopmall.ShoppingMallFragmentNew
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.RequstCommodityListParameter
import com.ciyuanplus.mobile.net.response.CommodityListItemRes
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.ciyuanplus.mobile.widget.ViewPagerForScrollView
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_commodityitem_list.*
import org.jetbrains.anko.support.v4.startActivity

class CommodityItemFragmentNew : Fragment() {

    private var categoryId: Int = 0
    private var searchValue: String? = null
    public var isLoadMoreEnable = false
    private val pageSize = 20
    private var pageIndex = 0
    private lateinit var mAdapter: CommodityItemRecyclerViewAdapter
    private var index = 0
    private val mList = ArrayList<CommodityListItemRes.CommodityItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            categoryId = it.getInt(CATEGORY_ID)
            searchValue = it.getString(SEARCH_VALUE)
            index = it.getInt("index")
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_commodityitem_list, container, false)
        mPager?.setObjectForPosition(view, index)
        Logger.d(index.toString())
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rcl.layoutManager = GridLayoutManager(activity, 2)
        rcl.addItemDecoration(object : RecyclerView.ItemDecoration() {


            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = Utils.dip2px(10F)
                outRect.right = Utils.dip2px(10F)
            }
        })

        mAdapter = CommodityItemRecyclerViewAdapter(activity, mList)

        mAdapter.setOnItemClickListener { adapter, view, position ->

            val item = adapter.getItem(position) as CommodityListItemRes.CommodityItem
            val userUuid = UserInfoData.getInstance().userInfoItem.uuid
            val sessionKey = SharedPreferencesManager.getString(
                    Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")

            val url = "${ApiContant.WEB_DETAIL_VIEW_URL}cyplus-share/prod.html?userUuid=$userUuid&authToken=$sessionKey&p=${item.prodId}"
            startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to url)
        }
        rcl.adapter = mAdapter
        rcl.isNestedScrollingEnabled = false
        requestList()
    }

    private fun requestList() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COMMODITY_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequstCommodityListParameter(pageSize.toString(), pageIndex.toString(), categoryId.toString(), searchValue).requestBody)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                val parent = parentFragment as ShoppingMallFragmentNew
                parent.finishRefreshAndLoadMore()

                val response1 = CommodityListItemRes(s)

                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if (response1.communityListItem?.list != null) {
                        if (response1.communityListItem!!.list.size >= pageSize) {
                            isLoadMoreEnable = true
                            pageIndex++

                        } else {
                            isLoadMoreEnable = false
                        }
                    } else {
                        isLoadMoreEnable = false
                    }

                    response1.communityListItem?.list?.let { mList.addAll(it) }
                    mAdapter.notifyDataSetChanged()

                } else {
                    isLoadMoreEnable = false
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                isLoadMoreEnable = false
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)

    }

    companion object {

        private const val CATEGORY_ID = "category-id"
        private const val SEARCH_VALUE = "search-value"
        private  var mPager: ViewPagerForScrollView? =null


        @JvmStatic
        fun newInstance(categoryId: Int, searchValue: String?, vp: ViewPagerForScrollView, position: Int) =
                CommodityItemFragmentNew().apply {
                    arguments = Bundle().apply {
                        putInt(CATEGORY_ID, categoryId)
                        putString(SEARCH_VALUE, searchValue)
                        putInt("index", position)
                        setViewPager(vp)
                    }
                }

        val fragment = CommodityItemFragmentNew()


        private fun setViewPager(vp: ViewPagerForScrollView) {
            mPager = vp
        }
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
