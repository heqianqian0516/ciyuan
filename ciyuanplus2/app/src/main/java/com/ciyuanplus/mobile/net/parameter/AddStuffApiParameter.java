package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class AddStuffApiParameter extends ApiParameter {
    private String communityUuid;
    private String contentText;
    private String imgList = "";
    private String price;
    private String userUuid;
    private String title;
    private String indexImg;

    public AddStuffApiParameter(String communityUuid, String title, String contentText,
                                String indexImg, ArrayList<String> images, String price) {
        this.title = title;
        this.contentText = contentText;
        this.indexImg = indexImg;
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList += "" + images.get(i) + ",";
            }
            this.imgList += "" + images.get(images.size() - 1) + "";
            this.imgList += "";
        }
        this.price = price;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.communityUuid = communityUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        if (!Utils.isStringEmpty(contentText))
            map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        else map.put("contentText", new ApiParamMap.ParamData(""));
        map.put("title", new ApiParamMap.ParamData(this.title));
        map.put("indexImg", new ApiParamMap.ParamData(this.indexImg));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("price", new ApiParamMap.ParamData(this.price));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        return map;
    }
}
