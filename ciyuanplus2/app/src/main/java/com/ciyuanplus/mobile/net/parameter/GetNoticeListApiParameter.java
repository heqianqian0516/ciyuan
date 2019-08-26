package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetNoticeListApiParameter extends ApiParameter {
    public static final String TYPE_SYSTEM = "0";
    public static final String TYPE_FOLLOW = "4";
    public static final String TYPE_NEWS = "6";


    private final String userUuid;
    private final String noticeType;
    private final String pager;
    private final String pageSize;

    public GetNoticeListApiParameter(String pager, String pageSize, String noticeType) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.pager = pager;
        this.pageSize = pageSize;
        this.noticeType = noticeType;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("noticeType", new ApiParamMap.ParamData(this.noticeType));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));

        return map;
    }

}
