package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestPostReplyApiParameter extends ApiParameter {
    private final String pageSize;
    private final String pager;
    private final String commentUuid;
    private final String postUuid;

    public RequestPostReplyApiParameter(String postUuid, String pager, String pageSize, String commentUuid) {
        this.postUuid = postUuid;
        this.pager = pager;
        this.pageSize = pageSize;
        this.commentUuid = commentUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        return map;
    }

}
