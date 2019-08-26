package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 * <p>
 * 喜欢   踩   取消喜欢   取消踩
 */
public class CommentLikeOperaApiParameter extends ApiParameter {
    private final String commentUuid;
    private final String userUuid;
    private final String bizType;
    private final String renderType;
    private final String postUuid;

    public CommentLikeOperaApiParameter(String postUuid, String commentUuid, String bizType, String renderType) {
        this.commentUuid = commentUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.postUuid = postUuid;
        this.bizType = bizType;
        this.renderType = renderType;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        map.put("renderType", new ApiParamMap.ParamData(this.renderType));
        return map;
    }

}
