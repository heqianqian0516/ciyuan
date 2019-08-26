package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestOrderListApiParameter extends ApiParameter {
    private final String merId;
    private final String status;
    private final String pager;
    private final String pageSize;
    private String userUuid;

    public RequestOrderListApiParameter(String merId, String status, String pager, String pageSize) {
        this.pager = pager;
        this.pageSize = pageSize;
        this.merId = merId;
        this.status = status;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("merId", new ApiParamMap.ParamData(this.merId));
        map.put("status", new ApiParamMap.ParamData(this.status));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
