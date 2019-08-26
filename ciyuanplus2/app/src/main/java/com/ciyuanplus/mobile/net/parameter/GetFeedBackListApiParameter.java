package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetFeedBackListApiParameter extends ApiParameter {

    private final String userUuid;
    private final String pager;
    private final String pageSize;

    public GetFeedBackListApiParameter(String pager, String pageSize) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.pager = pager;
        this.pageSize = pageSize;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));

        return map;
    }

}
