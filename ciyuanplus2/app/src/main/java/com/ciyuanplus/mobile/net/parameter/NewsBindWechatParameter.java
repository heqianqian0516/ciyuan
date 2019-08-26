package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * 首页绑定微信
 * Created by kk on 2018/5/2.
 */

public class NewsBindWechatParameter extends ApiParameter {

    private final String unionId;//微信UNIONID
    private final String userUuid;//用户UUID
    private final String wxName;//微信昵称

    public NewsBindWechatParameter(String unionId, String wxName) {

        this.unionId = unionId;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.wxName = wxName;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("unionId", new ApiParamMap.ParamData(this.unionId));
//        map.put("authToken", new ApiParamMap.ParamData(this.authToken));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("wxName", new ApiParamMap.ParamData(this.wxName));
        return map;
    }
}
