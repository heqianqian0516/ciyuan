package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class DeleteSystemNoticeApiParameter extends ApiParameter {
    private final String noticeType;
    private final String uuid;
    private final String userUuid;

    public DeleteSystemNoticeApiParameter(String uuid, String noticeType) {
        this.noticeType = noticeType;
        this.uuid = uuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("uuid", new ApiParamMap.ParamData(this.uuid));
        map.put("noticeType", new ApiParamMap.ParamData(this.noticeType));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
