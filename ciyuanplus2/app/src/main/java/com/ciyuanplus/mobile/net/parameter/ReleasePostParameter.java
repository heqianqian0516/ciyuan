package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class ReleasePostParameter extends ApiParameter {

    private String contentText;
    private String imgList = "";
    private String userUuid;
    private String typeId;

    public ReleasePostParameter(String contentText, ArrayList<String> images, String typeId) {
        this.contentText = contentText;
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList = String.format("%s%s", this.imgList, "" + images.get(i) + ",");
            }
            this.imgList += "" + images.get(images.size() - 1) + "";
            this.imgList += "";
        }
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.typeId = typeId;

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("typeId", new ApiParamMap.ParamData(this.typeId));
        return map;
    }
}
