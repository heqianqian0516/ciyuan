package com.ciyuanplus.mobile.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 卖家订单
 * Created by kk on 2018/5/24.
 */

public class MyOrderItem implements Serializable {

    private int id;
    private String orderNum;
    private String name;
    private String userId;
    private int price;
    private String orgPrice;
    private String payType;
    private int status;
    private String statusStr;
    private String bizType;
    private String cancelTime;
    private String createTime;
    private String recName;
    private String cancelReason;
    private String cancelActor;
    private String remark;
    private String recAddress;
    private String recPhone;
    private String expressId;
    private String expressName;
    private String expressCode;
    private String merId;
    private String shopName;
    private String shopImg;
    private String operateState;
    private List<SubOrderInfo> subOrderInfo;
    private String operateList;
    private String diliverType;
    private String diliverTypeName;
    private String merStoreId;
    private String merStoreAddress;
    private String payTime;
    private String diliverTimeDesc;
    private String cancelBtn;
    private String cancelBtnText;
    private String contactKFBtn;
    private String contactKFBtnText;
    private String copyExpressCodeBtn;
    private String copyExpressCodeBtnText;
    private String confirmReceivBtn;
    private String confirmReceivBtnText;
    private String payBtn;
    private String payBtnText;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setOrgPrice(String orgPrice) {
        this.orgPrice = orgPrice;
    }

    public String getOrgPrice() {
        return orgPrice;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayType() {
        return payType;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecName() {
        return recName;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelActor(String cancelActor) {
        this.cancelActor = cancelActor;
    }

    public String getCancelActor() {
        return cancelActor;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRecAddress(String recAddress) {
        this.recAddress = recAddress;
    }

    public String getRecAddress() {
        return recAddress;
    }

    public void setRecPhone(String recPhone) {
        this.recPhone = recPhone;
    }

    public String getRecPhone() {
        return recPhone;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getMerId() {
        return merId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setOperateState(String operateState) {
        this.operateState = operateState;
    }

    public String getOperateState() {
        return operateState;
    }

    public void setSubOrderInfo(List<SubOrderInfo> subOrderInfo) {
        this.subOrderInfo = subOrderInfo;
    }

    public List<SubOrderInfo> getSubOrderInfo() {
        return subOrderInfo;
    }

    public void setOperateList(String operateList) {
        this.operateList = operateList;
    }

    public String getOperateList() {
        return operateList;
    }

    public void setDiliverType(String diliverType) {
        this.diliverType = diliverType;
    }

    public String getDiliverType() {
        return diliverType;
    }

    public void setDiliverTypeName(String diliverTypeName) {
        this.diliverTypeName = diliverTypeName;
    }

    public String getDiliverTypeName() {
        return diliverTypeName;
    }

    public void setMerStoreId(String merStoreId) {
        this.merStoreId = merStoreId;
    }

    public String getMerStoreId() {
        return merStoreId;
    }

    public void setMerStoreAddress(String merStoreAddress) {
        this.merStoreAddress = merStoreAddress;
    }

    public String getMerStoreAddress() {
        return merStoreAddress;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setDiliverTimeDesc(String diliverTimeDesc) {
        this.diliverTimeDesc = diliverTimeDesc;
    }

    public String getDiliverTimeDesc() {
        return diliverTimeDesc;
    }

    public void setCancelBtn(String cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public String getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtnText(String cancelBtnText) {
        this.cancelBtnText = cancelBtnText;
    }

    public String getCancelBtnText() {
        return cancelBtnText;
    }

    public void setContactKFBtn(String contactKFBtn) {
        this.contactKFBtn = contactKFBtn;
    }

    public String getContactKFBtn() {
        return contactKFBtn;
    }

    public void setContactKFBtnText(String contactKFBtnText) {
        this.contactKFBtnText = contactKFBtnText;
    }

    public String getContactKFBtnText() {
        return contactKFBtnText;
    }

    public void setCopyExpressCodeBtn(String copyExpressCodeBtn) {
        this.copyExpressCodeBtn = copyExpressCodeBtn;
    }

    public String getCopyExpressCodeBtn() {
        return copyExpressCodeBtn;
    }

    public void setCopyExpressCodeBtnText(String copyExpressCodeBtnText) {
        this.copyExpressCodeBtnText = copyExpressCodeBtnText;
    }

    public String getCopyExpressCodeBtnText() {
        return copyExpressCodeBtnText;
    }

    public void setConfirmReceivBtn(String confirmReceivBtn) {
        this.confirmReceivBtn = confirmReceivBtn;
    }

    public String getConfirmReceivBtn() {
        return confirmReceivBtn;
    }

    public void setConfirmReceivBtnText(String confirmReceivBtnText) {
        this.confirmReceivBtnText = confirmReceivBtnText;
    }

    public String getConfirmReceivBtnText() {
        return confirmReceivBtnText;
    }

    public void setPayBtn(String payBtn) {
        this.payBtn = payBtn;
    }

    public String getPayBtn() {
        return payBtn;
    }

    public void setPayBtnText(String payBtnText) {
        this.payBtnText = payBtnText;
    }

    public String getPayBtnText() {
        return payBtnText;
    }

    public class SubOrderInfo {

        private int id;
        private int orderId;
        private int prodId;
        private String prodImg;
        private String prodName;
        private int prodSpecId;
        private String prodSpecName;
        private int prodPrice;
        private int prodCount;
        private String totalPrice;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setProdId(int prodId) {
            this.prodId = prodId;
        }

        public int getProdId() {
            return prodId;
        }

        public void setProdImg(String prodImg) {
            this.prodImg = prodImg;
        }

        public String getProdImg() {
            return prodImg;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdSpecId(int prodSpecId) {
            this.prodSpecId = prodSpecId;
        }

        public int getProdSpecId() {
            return prodSpecId;
        }

        public void setProdSpecName(String prodSpecName) {
            this.prodSpecName = prodSpecName;
        }

        public String getProdSpecName() {
            return prodSpecName;
        }

        public void setProdPrice(int prodPrice) {
            this.prodPrice = prodPrice;
        }

        public int getProdPrice() {
            return prodPrice;
        }

        public void setProdCount(int prodCount) {
            this.prodCount = prodCount;
        }

        public int getProdCount() {
            return prodCount;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getTotalPrice() {
            return totalPrice;
        }
    }
}
