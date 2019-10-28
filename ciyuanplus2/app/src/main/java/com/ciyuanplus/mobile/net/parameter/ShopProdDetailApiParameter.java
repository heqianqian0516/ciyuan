package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ShopProdDetailApiParameter extends ApiParameter {
    private final String userUuid;
    private final String prodId	;

    public ShopProdDetailApiParameter(String prodId) {
        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.prodId=prodId;

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("prodId",new ApiParamMap.ParamData(this.prodId));
        return map;
    }

}
