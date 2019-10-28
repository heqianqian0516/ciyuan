/**
 * Copyright 2019 bejson.com
 */
package com.ciyuanplus.mobile.net.response;

import androidx.annotation.Nullable;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Auto-generated: 2019-03-12 1:22:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ShopProdListItemRes extends ResponseData {
    @Nullable
    public Data communityListItem;

    public ShopProdListItemRes(String data) {
        super(data);

        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        //到这里已经解析出来 code  和 msg 了
        Gson gson = GsonUtils.getGsson();
        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            communityListItem = gson.fromJson(data1, Data.class);

        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        }

    }


    public class Data {

        private String pager;
        private List<CommodityItem> list;

        public void setPager(String pager) {
            this.pager = pager;
        }

        public String getPager() {
            return pager;
        }

        public void setList(List<CommodityItem> list) {
            this.list = list;
        }

        public List<CommodityItem> getList() {
            return list;
        }

    }


    public class CommodityItem {

        private int prodId;
        private String name;
        private String contentText;
        private String img;
        private int price;
        private int orgPrice;
        private String tags;
        private String isExpressFee;
        private String expressFee;
        private int categoryId;
        private String typeId;
        private String isPublish;
        private String isDelete;
        private int merId;
        private String shopImg;
        private String shopName;
        private String contentImg;
        private String specList;
        private String source;
        private String taobaoLink;
        private  String couponLink;

        public String getTaobaoLink() {
            return taobaoLink;
        }

        public void setTaobaoLink(String taobaoLink) {
            this.taobaoLink = taobaoLink;
        }

        public String getCouponLink() {
            return couponLink;
        }

        public void setCouponLink(String couponLink) {
            this.couponLink = couponLink;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setProdId(int prodId) {
            this.prodId = prodId;
        }

        public int getProdId() {
            return prodId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setContentText(String contentText) {
            this.contentText = contentText;
        }

        public String getContentText() {
            return contentText;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

        public void setOrgPrice(int orgPrice) {
            this.orgPrice = orgPrice;
        }

        public int getOrgPrice() {
            return orgPrice;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getTags() {
            return tags;
        }

        public void setIsExpressFee(String isExpressFee) {
            this.isExpressFee = isExpressFee;
        }

        public String getIsExpressFee() {
            return isExpressFee;
        }

        public void setExpressFee(String expressFee) {
            this.expressFee = expressFee;
        }

        public String getExpressFee() {
            return expressFee;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setIsPublish(String isPublish) {
            this.isPublish = isPublish;
        }

        public String getIsPublish() {
            return isPublish;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setMerId(int merId) {
            this.merId = merId;
        }

        public int getMerId() {
            return merId;
        }

        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }

        public String getShopImg() {
            return shopImg;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopName() {
            return shopName;
        }

        public void setContentImg(String contentImg) {
            this.contentImg = contentImg;
        }

        public String getContentImg() {
            return contentImg;
        }

        public void setSpecList(String specList) {
            this.specList = specList;
        }

        public String getSpecList() {
            return specList;
        }

    }

}

