package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestPostCommentApiParameter extends ApiParameter {
    private final String pageSize;
    private final String pager;
    private final String postUuid;
    private final String userUuid;
    private final String userState;

    public RequestPostCommentApiParameter(String pager, String pageSize, String postUuid) {
        this.pager = pager;
        this.pageSize = pageSize;
        this.postUuid = postUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";


    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        return map;
    }

}
