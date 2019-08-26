package com.ciyuanplus.mobile.net.parameter

import com.ciyuanplus.mobile.net.ApiParamMap
import com.ciyuanplus.mobile.net.ApiParameter
import com.orhanobut.logger.Logger
import java.util.*

/**
 * 首页绑定微信
 * Created by kk on 2018/5/2.
 */

class DefaultParameter(private val mMap: HashMap<String, String>) : ApiParameter() {

    public override fun buildExtraParameter(): ApiParamMap? {
        val map = ApiParamMap()

        for (key in mMap.keys) {
            Logger.d("Key: " + key + " Value: " + mMap[key])
            map[key] = ApiParamMap.ParamData(mMap[key])
        }

        return map
    }
}
