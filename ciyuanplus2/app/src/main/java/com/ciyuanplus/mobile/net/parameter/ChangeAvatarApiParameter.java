package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ChangeAvatarApiParameter extends ApiParameter {
    private final String photo;
    private final String userUuid;

    public ChangeAvatarApiParameter(String photo) {
        this.photo = photo;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("photo", new ApiParamMap.ParamData(this.photo));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
