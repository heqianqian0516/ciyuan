package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.utils.Utils;

/**
 * Created by Alen on 2017/2/11.
 */
public class DeleteMyNewsApiParameter extends ApiParameter {
    private String communityUuid = "";
    private String userUuid;
    private String postUuid = "";

    public DeleteMyNewsApiParameter(String postUuid, String communityUuid) {
        if (!Utils.isStringEmpty(postUuid)) this.postUuid = postUuid;
        if (!Utils.isStringEmpty(communityUuid)) this.communityUuid = communityUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
