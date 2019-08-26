package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/20.
 */

public class CommentParameter extends ApiParameter {

    private final String postUuid;
    private final String userUuid;
    private final String userState;
    private final String pager;
    private final String pageSize;

    public CommentParameter(String postUuid, String userUuid, String userState, String pager, String pageSize) {
        this.postUuid = postUuid;
        this.userUuid = userUuid;
        this.userState = userState;
        this.pager = pager;
        this.pageSize = pageSize;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        return map;
    }
}
