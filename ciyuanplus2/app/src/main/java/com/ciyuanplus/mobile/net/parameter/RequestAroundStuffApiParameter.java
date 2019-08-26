package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestAroundStuffApiParameter extends ApiParameter {

    private final String pager;
    private final String pageSize;
    private String userUuid;
    private String communityUuid;

    public RequestAroundStuffApiParameter(String pager, String pageSize) {
        this.pager = pager;
        this.pageSize = pageSize;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        return map;
    }
}
