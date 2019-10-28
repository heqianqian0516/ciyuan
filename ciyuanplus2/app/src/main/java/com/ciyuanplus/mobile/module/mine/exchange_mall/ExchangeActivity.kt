package com.ciyuanplus.mobile.module.mine.exchange_mall

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.adapter.ExchangeMallAdapter
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.TotalScoreBean
import com.ciyuanplus.mobile.net.parameter.ShopProdListApiParameter
import com.ciyuanplus.mobile.net.parameter.UserScoredApiParameter
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.ciyuanplus.mobile.widget.TitleBarView
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response

import java.util.ArrayList
import java.util.Collections

import butterknife.BindView
import butterknife.ButterKnife
import com.ciyuanplus.mobile.R2.id.position
import com.ciyuanplus.mobile.R2.id.tv_total_score
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.response.*
import crossoverone.statuslib.StatusUtil
import kotlinx.android.synthetic.main.activity_exchange.*
import kotlinx.android.synthetic.main.exchange_mall_item.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

/**
 * 次元币商城页面
 */
class ExchangeActivity : AppCompatActivity() {

    private var layoutManager: GridLayoutManager? = null
    private var exchangeMallAdapter: ExchangeMallAdapter? = null
    private val totalScoreBeanList = ArrayList<TotalScoreBean>()
    private var pager = 0
    private var mList = ArrayList<ShopProdListItemRes.CommodityItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)

        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"))

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true)

        totalScore()
        selectIntegralProdList()
        title_bar!!.setTitle("次元币商城")
        //titleBar!!.setOnBackListener({ v -> onBackPressed() })

        //  titleBar!!.registerRightImage(R.drawable.icon_list_share, { v ->


        ll_consumption_record!!.setOnClickListener {
            val intent = Intent(this@ExchangeActivity, ConsumptionRecordActivity::class.java)
            startActivity(intent)
        }
        //纵向线性布局
        layoutManager = GridLayoutManager(applicationContext, 2)
        rl_mall_view!!.layoutManager = layoutManager
        exchangeMallAdapter = ExchangeMallAdapter(this, mList)
        rl_mall_view!!.adapter = exchangeMallAdapter

       exchangeMallAdapter!!.setOnItemClickListener { adapter, view, position ->
          // CommonToast.getInstance("点击到了").show()
           val item = adapter.getItem(position) as ShopProdListItemRes.CommodityItem
           val userUuid = UserInfoData.getInstance().userInfoItem.uuid
           val sessionKey = SharedPreferencesManager.getString(
                   Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
           val url = "${ApiContant.WEB_DETAIL_CESHI}cyplus-share/prod.html?userUuid=$userUuid&authToken=$sessionKey&p=${item.prodId}"
           startActivity<ExchangeMallDetailActivity>(Constants.INTENT_OPEN_URL to url, Constants.INTENT_TITLE_BAR_TITLE to "详情", Constants.INTENT_PAY_TOTAL_MONEY to -1,
                   "prodId" to item.prodId )

       }
    }



    //总共的次元币
    private fun totalScore() {
        val postRequest = StringRequest(ApiContant.URL_HEAD
                + ApiContant.GET_INTEGRAL_DETAILS + "?userUuid=" + UserInfoData.getInstance().userInfoItem.uuid)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Get)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        Log.i("ttt", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                Log.i("ttt", s + "______" + response)
                val response1 = TotalScoreResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else if (response1.totalScoreListBean.data != null) {
                    Collections.addAll(totalScoreBeanList, response1.totalScoreListBean.data)
                    tv_total_score!!.text = response1.totalScoreListBean.data.userIntegral.toString() + ""

                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                CommonToast.getInstance("操作失败").show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    //次元币商品列表
    private fun selectIntegralProdList() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.SELECT_INTEGRAL_PRODL_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        val PAGE_SIZE = 20
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        postRequest.setHttpBody<AbstractRequest<String>>(ShopProdListApiParameter(pager.toString() + "", PAGE_SIZE.toString() + "").requestBody)
        postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        Log.i("ttt", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                Log.i("ttt", s + "______" + response)
                val response1 = ShopProdListItemRes(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if (response1.communityListItem!!.list != null) {
                        response1.communityListItem?.let { mList.addAll(it.list) }
                        exchangeMallAdapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                CommonToast.getInstance("操作失败").show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }
}


