package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kk on 2018/5/25.
 */

public class OrderDetailItem implements Serializable {

    //    bizType (integer, optional): 订单类型(0免单 1正常购买) ,
    @SerializedName("bizType")
    public int bizType;
    //    buyerUserUuid (string, optional): 买家UUID ,
    @SerializedName("buyerUserUuid")
    public String buyerUserUuid;
    //    cancelReason (string, optional): 取消原因 ,
    @SerializedName("cancelReason")
    public String cancelReason;
    //    cancelTime (string, optional): 取消时间 ,
    @SerializedName("cancelTime")
    public String cancelTime;
    //    createTime (string, optional): 订单创建时间 ,
    @SerializedName("createTime")
    public String createTime;
    //    logisticsType (integer, optional): 配送方式(1-商家配送、2-到店自取) ,
    @SerializedName("logisticsType")
    public int logisticsType;
    //    orderNum (string, optional): 订单编号 ,
    @SerializedName("orderNum")
    public String orderNum;
    //    orderPrice (integer, optional): 订单金额 ,
    @SerializedName("orderPrice")
    public int orderPrice;
    //    payType (integer, optional): 支付方式（1、微信，2、支付宝） ,
    @SerializedName("payType")
    public int payType;
    //    postUuid (string, optional): 商品UUID ,
    @SerializedName("postUuid")
    public String postUuid;
    //    prodCount (integer, optional): 商品数量 ,
    @SerializedName("prodCount")
    public int prodCount;
    //    prodImg (string, optional): 商品图片 ,
    @SerializedName("prodImg")
    public String prodImg;
    //    prodName (string, optional): 商品名称 ,
    @SerializedName("prodName")
    public String prodName;
    //    prodPrice (integer, optional): 商品单价 ,
    @SerializedName("prodPrice")
    public int prodPrice;
    //    recAddress (string, optional): 收件人地址 ,
    @SerializedName("recAddress")
    public String recAddress;
    //    recName (string, optional): 收件人姓名 ,
    @SerializedName("recName")
    public String recName;
    //    recPhone (string, optional): 收件人电话 ,
    @SerializedName("recPhone")
    public String recPhone;
    //    remark (string, optional): 订单备注 ,
    @SerializedName("remark")
    public String remark;
    //    sellerUserUuid (string, optional): 卖家UUID ,
    @SerializedName("sellerUserUuid")
    public String sellerUserUuid;
    //    status (integer, optional): 订单状态 ,
    @SerializedName("status")
    public int status;
    //    uuid (string, optional): 订单UUID ,
    @SerializedName("uuid")
    public String uuid;
    //    wlCode (string, optional): 物流编号
    @SerializedName("wlCode")
    public String wlCode;
}
