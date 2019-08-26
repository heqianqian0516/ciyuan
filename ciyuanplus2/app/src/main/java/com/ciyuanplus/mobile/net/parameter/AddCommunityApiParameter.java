package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class AddCommunityApiParameter extends ApiParameter {
    public static final String COMMUNITY_TYPE_DEFAULT = "0";// 高德自带小区
    public static String COMMUNITY_TYPE_OTHER = "1";// 自定义小区

    private final String commName;
    private final String provincesCode;
    private final String cityCode;
    private final String areaCode;
    private final String commAddress;
    private final String longitude;
    private final String latitude;
    private final String state;

    public AddCommunityApiParameter(String commName, String provincesCode, String cityCode,
                                    String areaCode, String commAddress, String longitude,
                                    String latitude, String state) {
        this.commName = commName;
        this.provincesCode = provincesCode;
        this.cityCode = cityCode;
        this.areaCode = areaCode;
        this.commAddress = commAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.state = state;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("commName", new ApiParamMap.ParamData(this.commName));
        map.put("provincesCode", new ApiParamMap.ParamData(this.provincesCode));
        map.put("cityCode", new ApiParamMap.ParamData(this.cityCode));
        map.put("areaCode", new ApiParamMap.ParamData(this.areaCode));
        map.put("commAddress", new ApiParamMap.ParamData(this.commAddress));
        map.put("longitude", new ApiParamMap.ParamData(this.longitude));
        map.put("latitude", new ApiParamMap.ParamData(this.latitude));
        map.put("state", new ApiParamMap.ParamData(this.state));
        return map;
    }

}
