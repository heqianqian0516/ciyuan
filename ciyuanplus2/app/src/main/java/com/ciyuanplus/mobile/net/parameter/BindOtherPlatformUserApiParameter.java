package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class BindOtherPlatformUserApiParameter extends ApiParameter {
    private final String userUuid;
    private final String mobile;
    private final String password;
    private final String code;
    private final String type;
    private final String nickname;
    private final String photo;

    public BindOtherPlatformUserApiParameter(String mobile, String password, String code, String type,
                                             String nickname, String photo) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.mobile = mobile;
        this.password = password;
        this.code = code;
        this.type = type;
        this.nickname = nickname;
        this.photo = photo;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("mobile", new ApiParamMap.ParamData(this.mobile));
        map.put("password", new ApiParamMap.ParamData(this.password));
        map.put("code", new ApiParamMap.ParamData(this.code));
        map.put("type", new ApiParamMap.ParamData(this.type));
        map.put("nickname", new ApiParamMap.ParamData(this.nickname));
        map.put("photo", new ApiParamMap.ParamData(this.photo));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
