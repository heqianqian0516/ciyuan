package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class SendSmsCodeApiParameter extends ApiParameter {
    public static final String SEND_SMS_TYPE_REGISTER = "1";
    public static final String SEND_SMS_TYPE_FORGET = "2";
    public static final String SEND_SMS_TYPE_CHANGE = "3";
    public static final String SEND_SMS_TYPE_LOGIN = "4";
    private final String mobile;
    private final String type;

    public SendSmsCodeApiParameter(String mobile, String type) {
        this.mobile = mobile;
        this.type = type;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("mobile", new ApiParamMap.ParamData(this.mobile));
        map.put("type", new ApiParamMap.ParamData(this.type));
        return map;
    }
}
