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
public class ShopProdDetailItemRes extends ResponseData {
    @Nullable
    public Data communityListItem;

    public ShopProdDetailItemRes(String data) {
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

        private int prodId;
        private String name;
        private String contentText;
        private String img;
        private int price;
        private int orgPrice;
        private Object tags;
        private Object isExpressFee;
        private Object expressFee;
        private int categoryId;
        private Object typeId;
        private Object isPublish;
        private Object isDelete;
        private int merId;
        private String shopImg;
        private String shopName;
        private String contentImg;
        private String source;
        private String taobaoLink;
        private String couponLink;
        private String isIntegral;
        private List<CommodityItem> specList;

        public int getProdId() {
            return prodId;
        }

        public void setProdId(int prodId) {
            this.prodId = prodId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContentText() {
            return contentText;
        }

        public void setContentText(String contentText) {
            this.contentText = contentText;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getOrgPrice() {
            return orgPrice;
        }

        public void setOrgPrice(int orgPrice) {
            this.orgPrice = orgPrice;
        }

        public Object getTags() {
            return tags;
        }

        public void setTags(Object tags) {
            this.tags = tags;
        }

        public Object getIsExpressFee() {
            return isExpressFee;
        }

        public void setIsExpressFee(Object isExpressFee) {
            this.isExpressFee = isExpressFee;
        }

        public Object getExpressFee() {
            return expressFee;
        }

        public void setExpressFee(Object expressFee) {
            this.expressFee = expressFee;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public Object getTypeId() {
            return typeId;
        }

        public void setTypeId(Object typeId) {
            this.typeId = typeId;
        }

        public Object getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(Object isPublish) {
            this.isPublish = isPublish;
        }

        public Object getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Object isDelete) {
            this.isDelete = isDelete;
        }

        public int getMerId() {
            return merId;
        }

        public void setMerId(int merId) {
            this.merId = merId;
        }

        public String getShopImg() {
            return shopImg;
        }

        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getContentImg() {
            return contentImg;
        }

        public void setContentImg(String contentImg) {
            this.contentImg = contentImg;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

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

        public String getIsIntegral() {
            return isIntegral;
        }

        public void setIsIntegral(String isIntegral) {
            this.isIntegral = isIntegral;
        }



        public void setSpecList(List<CommodityItem> specList) {
            this.specList = specList;
        }

        public List<CommodityItem> getSpecList() {
            return specList;
        }
    }


    public class CommodityItem {

        private int id;
        private int prodId;
        private String name;
        private int price;
        private int orgPrice;
        private int stock;
        private int startSellingCount;
        private int merId;
        private Object specImgs;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProdId() {
            return prodId;
        }

        public void setProdId(int prodId) {
            this.prodId = prodId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getOrgPrice() {
            return orgPrice;
        }

        public void setOrgPrice(int orgPrice) {
            this.orgPrice = orgPrice;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getStartSellingCount() {
            return startSellingCount;
        }

        public void setStartSellingCount(int startSellingCount) {
            this.startSellingCount = startSellingCount;
        }

        public int getMerId() {
            return merId;
        }

        public void setMerId(int merId) {
            this.merId = merId;
        }

        public Object getSpecImgs() {
            return specImgs;
        }

        public void setSpecImgs(Object specImgs) {
            this.specImgs = specImgs;
        }
    }

}

