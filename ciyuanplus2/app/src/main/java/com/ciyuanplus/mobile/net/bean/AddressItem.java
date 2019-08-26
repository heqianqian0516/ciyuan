package com.ciyuanplus.mobile.net.bean;

import java.io.Serializable;

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/21 10:02 PM
 * class   : AddressItem
 * desc   :
 * version: 1.0
 */

public class AddressItem implements Serializable {

    private int id;
    private String name;
    private String mobile;
    private String address;
    private String longitude;
    private String latitude;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLatitude() {
        return latitude;
    }

}


