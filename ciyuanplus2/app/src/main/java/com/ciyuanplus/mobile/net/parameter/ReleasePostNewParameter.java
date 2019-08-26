package com.ciyuanplus.mobile.net.parameter;

        import com.ciyuanplus.mobile.manager.UserInfoData;
        import com.ciyuanplus.mobile.net.ApiParamMap;
        import com.ciyuanplus.mobile.net.ApiParameter;

        import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class ReleasePostNewParameter extends ApiParameter {

    private String contentText;
    private String imgList = "";
    private String userUuid;
    private String typeId;
    private String activityUuid;

    public ReleasePostNewParameter(String contentText, ArrayList<String> images, String typeId, String activityUuid) {
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
        this.activityUuid = activityUuid;

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("imgList", new ApiParamMap.ParamData(this.imgList));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("typeId", new ApiParamMap.ParamData(this.typeId));
        map.put("activityUuid",new ApiParamMap.ParamData(this.activityUuid));

        return map;
    }
}
