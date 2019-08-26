package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class SearchStuffListApiParameter extends ApiParameter {
    private String communityUuid;
    private String userUuid;
    private String userState;
    private String pager;
    private String pageSize;
    private String searchValue;
    private String longitude;
    private String latitude;

    public SearchStuffListApiParameter(String longitude, String latitude, String searchValue, String pager, String pageSize) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
        this.searchValue = searchValue;
        this.pager = pager;
        this.pageSize = pageSize;
        this.longitude = longitude;
        this.latitude = latitude;
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("searchValue", new ApiParamMap.ParamData(this.searchValue));
        map.put("longitude", new ApiParamMap.ParamData(this.longitude));
        map.put("latitude", new ApiParamMap.ParamData(this.latitude));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        return map;
    }

}
