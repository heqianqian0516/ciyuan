package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 * <p>
 * 喜欢   踩   取消喜欢   取消踩
 */
public class CancelCommentLikeOperaApiParameter extends ApiParameter {
    private final String commentUuid;
    private final String userUuid;

    public CancelCommentLikeOperaApiParameter(String commentUuid) {
        this.commentUuid = commentUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }

}
