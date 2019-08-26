package com.ciyuanplus.mobile.net.parameter;


import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.content.multi.StringPart;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class UpdateOldPostApiParameter extends ApiParameter {
    private String postUuid;
    private String contentText;
    private String imgList = "";
    private String isAnonymous;
    private String isPublic;
    private String userUuid;
    private String title;
    private final String tags = "";
    private String typeId;

    public UpdateOldPostApiParameter(String title, String postUuid, String contentText, ArrayList<String> images, String isAnonymous, String isPublic, String typeId) {
        this.title = title;
        this.contentText = contentText;
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size() - 1; i++) {
                this.imgList += "" + images.get(i) + ",";
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
        map.put("title", new ApiParamMap.ParamData(this.title));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("isAnonymous", new ApiParamMap.ParamData(this.isAnonymous));
        map.put("isPublic", new ApiParamMap.ParamData(this.isPublic));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("tags", new ApiParamMap.ParamData(this.tags));

        return map;
    }

    @Override
    public MultipartBody getRequestBody() {
        MultipartBody body = new MultipartBody();
        body.addPart(new StringPart("contentText", this.contentText));
        body.addPart(new StringPart("title", this.title));
        body.addPart(new StringPart("tags", this.tags));
        body.addPart(new StringPart("imgList", this.imgList));
        body.addPart(new StringPart("isAnonymous", this.isAnonymous));
        body.addPart(new StringPart("isPublic", this.isPublic));
        body.addPart(new StringPart("userUuid", this.userUuid));
        body.addPart(new StringPart("postUuid", this.postUuid));
        body.addPart(new StringPart("typeId", this.typeId));
        return body;
    }
}
