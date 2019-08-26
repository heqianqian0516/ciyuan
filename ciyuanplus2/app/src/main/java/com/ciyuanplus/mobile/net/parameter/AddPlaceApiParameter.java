package com.ciyuanplus.mobile.net.parameter;

import com.amap.api.services.core.PoiItem;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class AddPlaceApiParameter extends ApiParameter {
    private String country;
    private String provinceCode;
    private String cityCode;
    private String areaCode;
    private String name;
    private String address;
    private String longitude;
    private String latitude;
    private String genType;
    private String placeType;
    private String placeUuid;
    private String userUuid;

    public AddPlaceApiParameter(PoiItem item, String genType) {
        this.country = "CHN";
        this.provinceCode = item.getProvinceCode();
        this.cityCode = item.getCityCode();
        this.areaCode = item.getAdCode();
        this.name = item.getTitle();
        this.address = item.getSnippet();
        this.longitude = item.getLatLonPoint().getLongitude() + "";
        this.latitude = item.getLatLonPoint().getLatitude() + "";
        this.genType = genType;
        this.placeUuid = item.getPoiId();
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("country", new ApiParamMap.ParamData(this.country));
        map.put("provinceCode", new ApiParamMap.ParamData(this.provinceCode));
        map.put("cityCode", new ApiParamMap.ParamData(this.cityCode));
        map.put("areaCode", new ApiParamMap.ParamData(this.areaCode));
        map.put("name", new ApiParamMap.ParamData(this.name));
        map.put("address", new ApiParamMap.ParamData(this.address));
        map.put("longitude", new ApiParamMap.ParamData(this.longitude));
        map.put("latitude", new ApiParamMap.ParamData(this.latitude));
        map.put("genType", new ApiParamMap.ParamData(this.genType));
        map.put("placeType", new ApiParamMap.ParamData(this.placeType));
        map.put("placeUuid", new ApiParamMap.ParamData(this.placeUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
