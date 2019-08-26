package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class SendFeedBacktApiParameter extends ApiParameter {

    private final String userUuid;
    private final String contentText;
    private final String messageType;

    public SendFeedBacktApiParameter(String contentText, String messageType) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.contentText = contentText;
        this.messageType = messageType;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("messageType", new ApiParamMap.ParamData(this.messageType));

        return map;
    }

}
