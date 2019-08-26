package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestFriendsApiParameter extends ApiParameter {
    private final String userUuid;
    private final String nickname;
    private final String pager;
    private final String pageSize;

    public RequestFriendsApiParameter(String nickname, String pager, String pageSize) { //请求其他人
        this.pager = pager;
        this.pageSize = pageSize;
        this.nickname = nickname;
        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("nickname", new ApiParamMap.ParamData(this.nickname));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
