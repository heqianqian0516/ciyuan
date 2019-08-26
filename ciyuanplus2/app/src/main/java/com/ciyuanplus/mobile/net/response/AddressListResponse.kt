package com.ciyuanplus.mobile.net.response

import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.AddressItem
import com.ciyuanplus.mobile.utils.GsonUtils
import com.ciyuanplus.mobile.utils.Utils
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 *    author : Kris
 *    e-mail : chengkai_1215@126.com
 *    date   : 2019/3/18 9:00 AM
 *    class  : ShopCarItemListResponse
 *    desc   : 地址列表Bean
 *    version: 1.0
 */


class AddressListResponse(data: String?) : ResponseData(data) {


    public var mItemList: ArrayList<AddressItem> = ArrayList()

    init {

        if (!Utils.isStringEquals(mCode, ResponseData.CODE_OK)) {


        }
        val gson = GsonUtils.getGsson()

        try {
            val mObject = JSONObject(data)
            val data1 = mObject.getString("data")
//            val data2Str = JSONObject(data1)
//            val data2 = data2Str.getString("data")
//            Logger.d("data2 = $data2")
            val type = object : TypeToken<ArrayList<AddressItem>>() {}.type
            mItemList = gson.fromJson<ArrayList<AddressItem>>(data1, type)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    data class SpecList(var pager: String?, var list: List<SpecItem>?)
    data class SpecItem(var id: Int, var userId: Int, var prodId: Int, var specId: Int, var startSellingCount: Int
                        , var prodImg: String?, var prodPrice: Int, var prodCount: Int, var specName: String?, var prodName: String?)
}