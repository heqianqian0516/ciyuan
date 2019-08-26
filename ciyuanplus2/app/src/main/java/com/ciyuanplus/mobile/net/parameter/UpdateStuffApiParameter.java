package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class UpdateStuffApiParameter extends ApiParameter {
    private final String communityUuid;
    private final String contentText;
    private String imgList;
    private final String price;
    private final String userUuid;
    private final String title;
    private final String postUuid;
    private final String indexImg;

    public UpdateStuffApiParameter(String postUuid, String communityUuid, String title, String contentText,
                                   String indexImg, ArrayList<String> images, String price) {
        this.title = title;
        this.contentText = contentText;
        this.indexImg = indexImg;
        if (images != null && images.size() > 0) {
            this.imgList = "";
            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList += "" + images.get(i) + ",";
            }
            this.imgList += "" + images.get(images.size() - 1) + "";
            this.imgList += "";
        }
        this.price = price;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.communityUuid = communityUuid;
        this.postUuid = postUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        if (!Utils.isStringEmpty(contentText))
            map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        else map.put("contentText", new ApiParamMap.ParamData(""));
        map.put("title", new ApiParamMap.ParamData(this.title));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("indexImg", new ApiParamMap.ParamData(this.indexImg));
        map.put("price", new ApiParamMap.ParamData(this.price));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        return map;
    }
}
