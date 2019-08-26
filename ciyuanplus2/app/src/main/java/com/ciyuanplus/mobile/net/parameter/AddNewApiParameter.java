package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class AddNewApiParameter extends ApiParameter {
    private String communityUuid;
    private String contentText;
    private String imgList = "";
    private String isAnonymous;
    private String isPublic;
    private String userUuid;
    private String typeId;

    public AddNewApiParameter(String contentText, ArrayList<String> images, String isAnonymous, String isPublic, String typeId) {
        this.contentText = contentText;
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList = String.format("%s%s", this.imgList, "" + images.get(i) + ",");
            }
            this.imgList += "" + images.get(images.size() - 1) + "";
            this.imgList += "";
        }
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
        this.typeId = typeId;

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        if (!Utils.isStringEmpty(contentText))
            map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        else map.put("contentText", new ApiParamMap.ParamData(""));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("isAnonymous", new ApiParamMap.ParamData(this.isAnonymous));
        map.put("isPublic", new ApiParamMap.ParamData(this.isPublic));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("typeId", new ApiParamMap.ParamData(this.typeId));
        return map;
    }
}
