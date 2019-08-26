package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestAroundWikiApiParameter extends ApiParameter {
    private final String communityUuid;
    private final String userState;
    private final String userUuid;
    private final String pager;
    private final String pageSize;
    private String wikiTypeParentId;

    public RequestAroundWikiApiParameter(String pager, String pageSize, String wikiTypeParentId) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
        this.pageSize = pageSize;
        this.pager = pager;
        this.wikiTypeParentId = wikiTypeParentId;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("wikiTypeParentId", new ApiParamMap.ParamData(this.wikiTypeParentId));

        return map;
    }
}
