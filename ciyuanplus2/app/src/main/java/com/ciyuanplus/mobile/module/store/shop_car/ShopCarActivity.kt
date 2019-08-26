package com.ciyuanplus.mobile.module.store.shop_car

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.adapter.ShopCarAdapter
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.ShopCarItemListResponse
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.widget.CommonToast
import com.kris.baselibrary.util.NumberUtil
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_shop_car.*
import org.jetbrains.anko.startActivity


class ShopCarActivity : MyBaseActivity(), OnRefreshListener, OnLoadMoreListener {



    private var page = 0
    private val pageSize = 100
    private var totalPrice: Double = 0.0
    private val mCommodityList = ArrayList<ShopCarItemListResponse.SpecItem>()
    private lateinit var shopCaAdapter: ShopCarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_car)

        initView()

    }

    override fun onResume() {
        super.onResume()

        doRequest(true)
    }

    private fun initView() {

        with(titleBar) {
            setTitle("购物车")
            setOnBackListener(View.OnClickListener { onBackPressed() })
        }

        smartRefreshLayout.setOnRefreshListener(this)
        smartRefreshLayout.setOnLoadMoreListener(this)

        shopCaAdapter = ShopCarAdapter(mCommodityList)

        /**........*/


        val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(shopCaAdapter)
        val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
        itemTouchHelper.attachToRecyclerView(ordersRecyclerView)

        /**........*/

        shopCaAdapter.setOnItemChildClickListener { adapter, view, position ->

            val item = adapter.getItem(position) as ShopCarItemListResponse.SpecItem
            when (view.id) {
                R.id.addButton -> {

                    add(item, 1, position)


                }
                R.id.minusButton -> {
                    if (item.prodCount <= 1) {
                        CommonToast.getInstance("至少选择一个商品").show()
                    } else {
//                        deleteItem(item, 1, position)
                        add(item, -1, position)
                    }
                }
                R.id.btnDelete -> {
                    deleteItem(item, 1, position)
                }
            }
        }

        with(ordersRecyclerView) {
            layoutManager = LinearLayoutManager(this@ShopCarActivity)
            adapter = shopCaAdapter


        }

        btn_submit.setOnClickListener {
            if (mCommodityList.size == 0 || totalPrice == 0.0) {
                CommonToast.getInstance("请选择要下单的商品").show()
                return@setOnClickListener

            }
            val userUUID = UserInfoData.getInstance().userInfoItem.uuid
            val authToken = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
            val url = "${ApiContant.WEB_DETAIL_VIEW_URL}cyplus-share/makeOrder.html?userUuid=$userUUID&authToken=$authToken&isCart=1"
            startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to url)
            finish()
        }
    }


    private val onItemSwipeListener = object : OnItemSwipeListener {
        override fun clearView(p0: RecyclerView.ViewHolder?, p1: Int) {
        }

        override fun onItemSwiped(holder: RecyclerView.ViewHolder?, pos: Int) {

            Logger.d("结束 长度 =  ${mCommodityList.size} , pos = $pos")
            Logger.d("删除位置 ${holder?.adapterPosition.toString()}    ===    ${shopCaAdapter.getViewHolderPosition(holder)}")
            shopCaAdapter.getViewHolderPosition(holder);


            totalPrice = 0.0
            for (item in mCommodityList) {
                totalPrice += (item.prodPrice.toDouble() / 100 * item.prodCount)
            }
            setTotalPrice()

        }

        override fun onItemSwipeStart(p0: RecyclerView.ViewHolder?, p1: Int) {

            Logger.d("开始 长度 =  ${mCommodityList.size} , pos = $p1")
        }

        override fun onItemSwipeMoving(p0: Canvas?, p1: RecyclerView.ViewHolder?, p2: Float, p3: Float, p4: Boolean) {
            p0?.drawColor(resources.getColor(R.color.blue))
//                canvas.drawText("Just some text", 0, 40, paint);


        }
    }

    private fun doRequest(reset: Boolean) {

        if (reset) {
            page = 0
            mCommodityList.clear()
            totalPrice = 0.0
        }

        val paramsMap = HashMap<String, String>()
        paramsMap["pager"] = page.toString()
        paramsMap["pageSize"] = pageSize.toString()
        paramsMap["merId"] = "6"

        val postRequest = StringRequest(ApiContant.URL_HEAD + "/api/server/cartItem/selectCartItems")
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)

        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(paramsMap).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ShopCarActivity) {

            override fun onSuccess(s: String?, response: Response<String>?) {

                finishRefreshAndLoadMore()
                Logger.d(s)
                val res = ShopCarItemListResponse(s)
                res.let {
                    mCommodityList.addAll(res.mItemList)
                    ordersRecyclerView.adapter?.notifyDataSetChanged()
                    smartRefreshLayout.setEnableLoadMore(res.mItemList.size >= pageSize)

                }
                for (item in res.mItemList) {

                    totalPrice += item.prodPrice.toDouble() / 100 * item.prodCount
                }
                setTotalPrice()
                page++
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                Logger.d(e.toString())
                finishRefreshAndLoadMore()
            }
        })

        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun setTotalPrice() {
        Logger.d(totalPrice.toString())
        totalPriceNum.text = "￥ ${NumberUtil.getAmountValue(totalPrice.toDouble().toString())}"
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        doRequest(false)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        doRequest(true)
    }

    private fun deleteItem(item: ShopCarItemListResponse.SpecItem, count: Int, position: Int) {

        val paramsMap = HashMap<String, String>()

        //cartItemIds 购物车列表id,批量删除用，隔开

        paramsMap["cartItemIds"] = item.id.toString()

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_REMOVE_SHOP_CART_ITEM)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)

        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(paramsMap).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ShopCarActivity) {
            override fun onSuccess(s: String?, response: Response<String>?) {

                finishRefreshAndLoadMore()
                Logger.d(s)
                val res = ResponseData(s)
                if (StringUtils.equals(res.mCode, ResponseData.CODE_OK)) {
                    minusTotalPrice(item, count, position)
                } else {
                    CommonToast.getInstance(res.mMsg).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                Logger.d(e.toString())
                finishRefreshAndLoadMore()
            }
        })

        LiteHttpManager.getInstance().executeAsync(postRequest)

    }

    private fun add(item: ShopCarItemListResponse.SpecItem, count: Int, position: Int) {

        val paramsMap = HashMap<String, String>()
        paramsMap["prodId"] = item.prodId.toString()
        paramsMap["specId"] = item.specId.toString()
        paramsMap["prodCount"] = (item.prodCount + count).toString()
        paramsMap["isAppend"] = "0"

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_ADD_SHOP_CART_ITTEM)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)

        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(paramsMap).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ShopCarActivity) {

            override fun onSuccess(s: String?, response: Response<String>?) {

                finishRefreshAndLoadMore()
                Logger.d(s)
                val res = ResponseData(s)
                if (StringUtils.equals(res.mCode, ResponseData.CODE_OK)) {
                    addTotalPrice(item, count, position)
                } else {
                    CommonToast.getInstance(res.mMsg).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                Logger.d(e.toString())
                finishRefreshAndLoadMore()
            }
        })

        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun addTotalPrice(item: ShopCarItemListResponse.SpecItem, count: Int, position: Int) {
        mCommodityList[position].prodCount += count
        totalPrice += item.prodPrice.toDouble() / 100 * count
        setTotalPrice()
        shopCaAdapter.notifyItemChanged(position)
    }

    private fun minusTotalPrice(item: ShopCarItemListResponse.SpecItem, count: Int, position: Int) {
//        mCommodityList[position].prodCount -= count
//        totalPrice -= item.prodPrice.toDouble() / 100 * count
//        setTotalPrice()
//        shopCaAdapter.notifyItemChanged(position)
        mCommodityList.remove(item)
        totalPrice = 0.0
        for (item2 in mCommodityList) {

            totalPrice += item2.prodPrice.toDouble() / 100 * item.prodCount
        }
        setTotalPrice()
        shopCaAdapter.notifyDataSetChanged()

    }

    private fun finishRefreshAndLoadMore() {
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()
    }

}
