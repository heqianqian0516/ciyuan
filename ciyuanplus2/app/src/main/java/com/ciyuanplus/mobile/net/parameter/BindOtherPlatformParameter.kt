package com.ciyuanplus.mobile.net.parameter

import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.net.ApiParamMap
import com.ciyuanplus.mobile.net.ApiParameter
import com.orhanobut.logger.Logger

import java.util.HashMap

/**
 * 首页绑定微信
 * Created by kk on 2018/5/2.
 */

class BindOtherPlatformParameter(private val mMap: HashMap<String, String>) : ApiParameter() {

    private val userUuid: String? = UserInfoData.getInstance().userInfoItem?.uuid//用户UUID

    public override fun buildExtraParameter(): ApiParamMap? {
        val map = ApiParamMap()


        for (key in mMap.keys) {
            Logger.d("Key: " + key + " Value: " + mMap[key])
            map[key] = ApiParamMap.ParamData(mMap[key])
        }
        map["userUuid"] = ApiParamMap.ParamData(this.userUuid?:"")

        return map
    }
}
