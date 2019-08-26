package com.ciyuanplus.mobile.net.parameter

import com.ciyuanplus.mobile.net.ApiParamMap
import com.ciyuanplus.mobile.net.ApiParameter

/**
 *    author : Kris
 *    e-mail : chengkai_1215@126.com
 *    date   : 2019/3/10 5:26 PM
 *    class   : RequstCommodityListParameter
 *    desc   :
 *    version: 1.0
 */
class RequstCommodityListParameter(var pageSize: String, var pageIndex: String, var categoryId: String, var searchValue: String?) : ApiParameter() {

    override fun buildExtraParameter(): ApiParamMap? {
        val map = ApiParamMap()
        map["pager"] = ApiParamMap.ParamData(this.pageIndex)
        map["pageSize"] = ApiParamMap.ParamData(this.pageSize)
        map["searchValue"] = ApiParamMap.ParamData(searchValue)
        map["categoryId"] = ApiParamMap.ParamData(this.categoryId)
        return map
    }
}