package com.ciyuanplus.mobile.net.parameter;


import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class UpdateNewPostApiParameter extends ApiParameter {
    private String postUuid;
    private String contentText;
    private String imgList = "";
    private String isAnonymous;
    private String isPublic;
    private String userUuid;
    private String tags;
    private String title;
    private String typeId;
    private String bizType;
    private String score;

    public UpdateNewPostApiParameter(String postUuid, String contentText, ArrayList<String> images,
                                     String isAnonymous, String isPublic,
                                     String tags, String title, String typeId,
                                     String bizType, String score) {
        this.postUuid = postUuid;
        this.contentText = contentText;
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList = new StringBuilder().append(this.imgList).append("" + images.get(i) + ",").toString();
            }
            this.imgList += "" + images.get(images.size() - 1) + "";
            this.imgList += "";
        }
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.tags = tags;
        this.title = title;
        this.typeId = typeId;
        this.bizType = bizType;
        this.score = score;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("isAnonymous", new ApiParamMap.ParamData(this.isAnonymous));
        map.put("isPublic", new ApiParamMap.ParamData(this.isPublic));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("tags", new ApiParamMap.ParamData(this.tags));
        map.put("title", new ApiParamMap.ParamData(this.title));
        map.put("typeId", new ApiParamMap.ParamData(this.typeId));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        map.put("score", new ApiParamMap.ParamData(this.score));

        return map;
    }
}
