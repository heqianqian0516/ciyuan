package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestSavePushSettingApiParameter extends ApiParameter {
    private final String userUuid;
    private final String commentPush;
    private final String followerPush;
    private final String systemmessagePush;
    private final String chatmessagePush;

    public RequestSavePushSettingApiParameter(boolean commentPush, boolean followerPush, boolean systemmessagePush, boolean chatmessagePush) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.commentPush = commentPush ? "1" : "0";
        this.followerPush = followerPush ? "1" : "0";
        this.systemmessagePush = systemmessagePush ? "1" : "0";
        this.chatmessagePush = chatmessagePush ? "1" : "0";
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("commentPush", new ApiParamMap.ParamData(this.commentPush));
        map.put("followerPush", new ApiParamMap.ParamData(this.followerPush));
        map.put("systemmessagePush", new ApiParamMap.ParamData(this.systemmessagePush));
        map.put("chatmessagePush", new ApiParamMap.ParamData(this.chatmessagePush));
        return map;
    }
}
