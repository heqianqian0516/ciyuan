package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class AddNewPostApiParameter extends ApiParameter {
    private String communityUuid;
    private String contentText;
    private String imgList = "";
    private String isAnonymous;
    private String isPublic;
    private String userUuid;
    private String title;
    private String tags;
    private String typeId;
    private String score;
    private String bizType;
    private String placeId;

    public AddNewPostApiParameter(String communityUuid, String contentText, ArrayList<String> images,
                                  String isAnonymous, String isPublic,
                                  String title, String tags, String typeId,
                                  String score, String bizType, String placeId) {
        if (Utils.isStringEmpty(communityUuid))
            this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
        else this.communityUuid = communityUuid;
        this.contentText = contentText;
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList = this.imgList + "" + images.get(i) + ",";
            }
            this.imgList += "" + images.get(images.size() - 1) + "";
            this.imgList += "";
        }
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.title = title;
        this.tags = tags;
        this.typeId = typeId;
        this.score = score;
        this.bizType = bizType;
        this.placeId = placeId;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        if (!Utils.isStringEmpty(contentText))
            map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        else map.put("contentText", new ApiParamMap.ParamData(""));
        map.put("title", new ApiParamMap.ParamData(this.title));
        map.put("tags", new ApiParamMap.ParamData(this.tags));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("isAnonymous", new ApiParamMap.ParamData(this.isAnonymous));
        map.put("isPublic", new ApiParamMap.ParamData(this.isPublic));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("typeId", new ApiParamMap.ParamData(this.typeId));
        map.put("score", new ApiParamMap.ParamData(this.score));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        map.put("placeId", new ApiParamMap.ParamData(this.placeId));
        return map;
    }

}
