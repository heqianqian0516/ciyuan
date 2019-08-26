package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.utils.Utils;

/**
 * Created by Alen on 2017/2/11.
 */
public class AddCommentApiParameter extends ApiParameter {
    private final String postUuid;
    private final String commentUuid;// 主评论
    private final String targetCommentUuid; // 要回复的评论id
    private final String contentText;
    private final String commentType;
    private final String toUserUuid;
    private final String userUuid;

    public AddCommentApiParameter(String postUuid, String commentUuid, String targetCommentUuid, String contentText, String commentType, String toUserUuid) {
        this.postUuid = postUuid;
        this.commentUuid = commentUuid;
        this.targetCommentUuid = targetCommentUuid;
        this.contentText = contentText;
        this.commentType = commentType;
        this.toUserUuid = toUserUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        if (!Utils.isStringEmpty(commentUuid))
            map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        else map.put("commentUuid", new ApiParamMap.ParamData(""));
        map.put("targetCommentUuid", new ApiParamMap.ParamData(this.targetCommentUuid));
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("commentType", new ApiParamMap.ParamData(this.commentType));
        map.put("toUserUuid", new ApiParamMap.ParamData(this.toUserUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }

}
