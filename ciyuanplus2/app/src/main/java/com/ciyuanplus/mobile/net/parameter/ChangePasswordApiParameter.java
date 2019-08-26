package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ChangePasswordApiParameter extends ApiParameter {
    private final String oldPassword;
    private final String newPassword;
    private final String userUuid;

    public ChangePasswordApiParameter(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("oldPassword", new ApiParamMap.ParamData(this.oldPassword));
        map.put("newPassword", new ApiParamMap.ParamData(this.newPassword));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
