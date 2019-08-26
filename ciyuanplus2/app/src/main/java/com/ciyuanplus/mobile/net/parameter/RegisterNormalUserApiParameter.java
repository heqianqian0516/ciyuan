package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RegisterNormalUserApiParameter extends ApiParameter {
    private final String unionId;
    private final String loginType;
    private final String mobile;
    private final String password;
    private final String code;
    private final String type;
    private final String sex;
    private final String nickname;
    private final String photo;

    public RegisterNormalUserApiParameter(String mobile, String password, String code, String type,
                                          String sex, String nickname, String photo, String unionId, String loginType) {
        this.unionId = unionId;
        this.loginType = loginType;
        this.mobile = mobile;
        this.password = password;
        this.code = code;
        this.type = type;
        this.sex = sex;
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
        map.put("sex", new ApiParamMap.ParamData(this.sex));
        map.put("nickname", new ApiParamMap.ParamData(this.nickname));
        map.put("photo", new ApiParamMap.ParamData(this.photo));
        map.put("loginType", new ApiParamMap.ParamData(this.loginType));
        map.put("unionId", new ApiParamMap.ParamData(this.unionId));
        return map;
    }
}
