package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class UpdateNewApiParameter extends ApiParameter {
    private final String postUuid;
    private final String contentText;
    private String imgList = "";
    private final String isAnonymous;
    private final String isPublic;
    private final String userUuid;
    private String typeId;

    public UpdateNewApiParameter(String postUuid, String contentText, ArrayList<String> images, String isAnonymous, String isPublic, String typeId) {
        this.contentText = contentText;
        if (images != null && images.size() > 0) {

            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList = new StringBuilder().append(this.imgList).append(images.get(i)).append(",").toString();
            }
            this.imgList += "" + images.get(images.size() - 1) + "";
            this.imgList += "";
        }
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.postUuid = postUuid;
        this.typeId = typeId;

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
        map.put("typeId", new ApiParamMap.ParamData(this.typeId));
        return map;
    }

}
