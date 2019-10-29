package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * 短视频列表接口参数
 */
public class GetVideoListApiParameter extends ApiParameter {
    private String userUuid;
    private int pager;
    private int pageSize;

    public GetVideoListApiParameter(String userUuid, int pager, int pageSize) {
        this.userUuid = userUuid;
        this.pager = pager;
        this.pageSize = pageSize;
    }

    public GetVideoListApiParameter(String userUuid, int pager) {
        this.userUuid = userUuid;
        this.pager = pager;
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
