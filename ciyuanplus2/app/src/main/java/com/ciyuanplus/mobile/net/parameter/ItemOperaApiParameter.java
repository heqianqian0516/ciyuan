package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 * <p>
 * 喜欢   踩   取消喜欢   取消踩
 */
public class ItemOperaApiParameter extends ApiParameter {
    private final String postUuid;
    private final String userUuid;

    public ItemOperaApiParameter(String postUuid) {
        this.postUuid = postUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }

}
