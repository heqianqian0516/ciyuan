package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestWorldPostsApiParameter extends ApiParameter {
    private final String userState;
    private final String userUuid;
    private final String searchValue;
    private final String pager;
    private final String pageSize;
    private String lastId;
    private String postType;
    private String bizType;

    public RequestWorldPostsApiParameter(String searchValue, String pager, String pageSize,
                                         String lastId, String postType, String bizType) {
        this.lastId = lastId;
        this.searchValue = searchValue;
        this.pager = pager;
        this.pageSize = pageSize;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
        this.postType = postType;
        this.bizType = bizType;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("searchValue", new ApiParamMap.ParamData(this.searchValue));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("lastId", new ApiParamMap.ParamData(this.lastId));
        map.put("postType", new ApiParamMap.ParamData(this.postType));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        return map;
    }
}
