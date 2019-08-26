package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class SubmmitPostScoreApiParameter extends ApiParameter {
    private final String postUuid;
    private final String commentUuid = "";
    private final String targetCommentUuid = "";
    private final String contentText;
    private final String score;
    private final String commentType = "1";
    private final String toUserUuid;
    private final String bizType;
    private final String renderType = "2";
    private final String userUuid;

    public SubmmitPostScoreApiParameter(String postUuid, String contentText, String toUserUuid, String score, String bizType) { //请求其他人
        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.postUuid = postUuid;
        this.contentText = contentText;
        this.toUserUuid = toUserUuid;
        this.score = score;
        this.bizType = bizType;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("targetCommentUuid", new ApiParamMap.ParamData(this.targetCommentUuid));
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("score", new ApiParamMap.ParamData(this.score));
        map.put("commentType", new ApiParamMap.ParamData(this.commentType));
        map.put("toUserUuid", new ApiParamMap.ParamData(this.toUserUuid));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        map.put("renderType", new ApiParamMap.ParamData(this.renderType));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
