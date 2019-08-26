package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestRongYunTokenApiParameter extends ApiParameter {
    private final String userUuid;
    private final String nickName;
    private final String photo;

    public RequestRongYunTokenApiParameter() {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.nickName = UserInfoData.getInstance().getUserInfoItem().nickname;
        this.photo = UserInfoData.getInstance().getUserInfoItem().photo;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("nickName", new ApiParamMap.ParamData(this.nickName));
        map.put("photo", new ApiParamMap.ParamData(this.photo));
        return map;
    }

}
