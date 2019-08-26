package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestOtherLoginApiParameter extends ApiParameter {
    private String unionId;
    private String loginType;
    private String wxName;
    private String photo;

    public RequestOtherLoginApiParameter(String unionId, String wxName, String loginType,String photo) {
        this.unionId = unionId;
        this.loginType = loginType;
        this.wxName = wxName;
        this.photo = photo;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("unionId", new ApiParamMap.ParamData(this.unionId));
        map.put("loginType", new ApiParamMap.ParamData(this.loginType));
        map.put("nickname", new ApiParamMap.ParamData(this.wxName));
        map.put("photo", new ApiParamMap.ParamData(this.photo));
        return map;
    }

}
