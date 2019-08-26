package com.ciyuanplus.mobile.net.response

import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.CommodityTypeItem
import com.ciyuanplus.mobile.statistics.StatisticsManager
import com.ciyuanplus.mobile.utils.GsonUtils
import com.ciyuanplus.mobile.utils.Utils
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import org.json.JSONException
import org.json.JSONObject

/**
 *    author : Kris
 *    e-mail : chengkai_1215@126.com
 *    date   : 2019/3/10 5:07 PM
 *    class   : CommodityTypeListItem
 *    desc   :
 *    version: 1.0
 */

/**
 * Copyright 2019 bejson.com
 */


/**
 * Auto-generated: 2019-03-10 17:6:2
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
//class CommodityTypeListItem(data: String?) : ResponseData(data) {
//
//    var items: List<CommodityTypeItem>? = null
//
//    init {
//
//        if (!Utils.isStringEquals(mCode, ResponseData.CODE_OK)) {
//
//            val gson = GsonUtils.getGsson()
//            try {
//                val mObject = JSONObject(data)//
//                val data1 = mObject.getString("data")
//                val listType = object : TypeToken<List<CommodityTypeItem>>() {}.type
//
//                items = gson.fromJson<ArrayList<CommodityTypeItem>>(data1, listType)
//            } catch (e: JSONException) {
//                StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "")
//
//                e.printStackTrace()
//            }
//        }
//    }
//
//}

class CommodityTypeListItem(data: String?) : ResponseData(data) {
    var items = ArrayList<CommodityTypeItem>()

    init {
        (Utils.isStringEquals(mCode, ResponseData.CODE_OK).let {

            val gson = GsonUtils.getGsson()
            try {
                val mObject = JSONObject(data)//
                val data1 = mObject.getString("data")
                val listType = object : TypeToken<List<CommodityTypeItem>>() {}.type
                items = gson.fromJson(data1, listType)
                Logger.d("分类解析成功")
            } catch (e: JSONException) {
                StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "")
                Logger.d("分类解析失败 $e")
                e.printStackTrace()
            }
        })


    }
}