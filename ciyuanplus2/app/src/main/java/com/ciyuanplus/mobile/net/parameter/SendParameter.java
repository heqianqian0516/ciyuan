package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/20.
 */

public class SendParameter extends ApiParameter {

    private final String postUuid;
    private final String commentUuid;
    private final String targetCommentUuid;
    private final String contentText;
    private final String commentType;
    private final String toUserUuid;
    private final String bizType;
    private final String userUuid;

    public SendParameter(String postUuid, String commentUuid, String targetCommentUuid, String contentText, String commentType, String toUserUuid, String bizType, String userUuid) {
        this.postUuid = postUuid;
        this.commentUuid = commentUuid;
        this.targetCommentUuid = targetCommentUuid;
        this.contentText = contentText;
        this.commentType = commentType;
        this.toUserUuid = toUserUuid;
        this.bizType = bizType;
        this.userUuid = userUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {

        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("targetCommentUuid", new ApiParamMap.ParamData(this.targetCommentUuid));
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("commentType", new ApiParamMap.ParamData(this.commentType));
        map.put("toUserUuid", new ApiParamMap.ParamData(this.toUserUuid));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
