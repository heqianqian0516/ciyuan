package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * 个人中心查询是否绑定微信
 * Created by kk on 2018/5/2.
 */

public class MineBindWechatParameter extends ApiParameter {

    private final String userUuid;//用户UUID

    public MineBindWechatParameter() {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
