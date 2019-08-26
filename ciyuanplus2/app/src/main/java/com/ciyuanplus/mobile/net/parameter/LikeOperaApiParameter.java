package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 * <p>
 * 喜欢   踩   取消喜欢   取消踩
 */
public class LikeOperaApiParameter extends ApiParameter {
    private final String postUuid;
    private final String userUuid;
    private final String bizType;

    public LikeOperaApiParameter(String bizType, String postUuid) {
        this.bizType = bizType;
        this.postUuid = postUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        return map;
    }

}
