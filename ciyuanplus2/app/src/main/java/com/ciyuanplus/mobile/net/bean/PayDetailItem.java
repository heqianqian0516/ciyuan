package com.ciyuanplus.mobile.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/28 1:47 PM
 * class   : PayDetailItem
 * desc   :
 * version: 1.0
 */
public class PayDetailItem implements Serializable {


    /**
     * addCommentBtn : 0
     * addCommentBtnText : string
     * bizType : 0
     * cancelActor : 0
     * cancelBtn : 0
     * cancelBtnText : string
     * cancelReason : string
     * cancelTime : 2019-03-28T05:42:15.871Z
     * confirmReceivBtn : 0
     * confirmReceivBtnText : string
     * contactKFBtn : 0
     * contactKFBtnText : string
     * copyExpressCodeBtn : 0
     * copyExpressCodeBtnText : string
     * createTime : 2019-03-28T05:42:15.871Z
     * diliverTimeDesc : string
     * diliverType : 0
     * diliverTypeName : string
     * expressCode : string
     * expressId : 0
     * expressName : string
     * id : 0
     * merId : 0
     * merStoreAddress : string
     * merStoreId : 0
     * name : string
     * operateList : [{"createTime":"2019-03-28T05:42:15.871Z","id":0,"name":"string","remark":"string"}]
     * operateState : 0
     * orderNum : string
     * orgPrice : 0
     * payBtn : 0
     * payBtnText : string
     * payTime : 2019-03-28T05:42:15.871Z
     * payType : 0
     * price : 0
     * recAddress : string
     * recName : string
     * recPhone : string
     * remark : string
     * shopImg : string
     * shopName : string
     * status : 0
     * statusStr : string
     * subOrderInfo : [{"id":0,"orderId":0,"prodCount":0,"prodId":0,"prodImg":"string","prodName":"string","prodPrice":0,"prodSpecId":0,"prodSpecName":"string","totalPrice":"string"}]
     * userId : 0
     */

    private int addCommentBtn;
    private String addCommentBtnText;
    private int bizType;
    private int cancelActor;
    private int cancelBtn;
    private String cancelBtnText;
    private String cancelReason;
    private String cancelTime;
    private int confirmReceivBtn;
    private String confirmReceivBtnText;
    private int contactKFBtn;
    private String contactKFBtnText;
    private int copyExpressCodeBtn;
    private String copyExpressCodeBtnText;
    private String createTime;
    private String diliverTimeDesc;
    private int diliverType;
    private String diliverTypeName;
    private String expressCode;
    private int expressId;
    private String expressName;
    private int id;
    private int merId;
    private String merStoreAddress;
    private int merStoreId;
    private String name;
    private int operateState;
    private String orderNum;
    private int orgPrice;
    private int payBtn;
    private String payBtnText;
    private String payTime;
    private int payType;
    private int price;
    private String recAddress;
    private String recName;
    private String recPhone;
    private String remark;
    private String shopImg;
    private String shopName;
    private int status;
    private String statusStr;
    private int userId;
    private List<OperateListBean> operateList;
    private List<SubOrderInfoBean> subOrderInfo;

    public int getAddCommentBtn() {
        return addCommentBtn;
    }

    public void setAddCommentBtn(int addCommentBtn) {
        this.addCommentBtn = addCommentBtn;
    }

    public String getAddCommentBtnText() {
        return addCommentBtnText;
    }

    public void setAddCommentBtnText(String addCommentBtnText) {
        this.addCommentBtnText = addCommentBtnText;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getCancelActor() {
        return cancelActor;
    }

    public void setCancelActor(int cancelActor) {
        this.cancelActor = cancelActor;
    }

    public int getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(int cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public String getCancelBtnText() {
        return cancelBtnText;
    }

    public void setCancelBtnText(String cancelBtnText) {
        this.cancelBtnText = cancelBtnText;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public int getConfirmReceivBtn() {
        return confirmReceivBtn;
    }

    public void setConfirmReceivBtn(int confirmReceivBtn) {
        this.confirmReceivBtn = confirmReceivBtn;
    }

    public String getConfirmReceivBtnText() {
        return confirmReceivBtnText;
    }

    public void setConfirmReceivBtnText(String confirmReceivBtnText) {
        this.confirmReceivBtnText = confirmReceivBtnText;
    }

    public int getContactKFBtn() {
        return contactKFBtn;
    }

    public void setContactKFBtn(int contactKFBtn) {
        this.contactKFBtn = contactKFBtn;
    }

    public String getContactKFBtnText() {
        return contactKFBtnText;
    }

    public void setContactKFBtnText(String contactKFBtnText) {
        this.contactKFBtnText = contactKFBtnText;
    }

    public int getCopyExpressCodeBtn() {
        return copyExpressCodeBtn;
    }

    public void setCopyExpressCodeBtn(int copyExpressCodeBtn) {
        this.copyExpressCodeBtn = copyExpressCodeBtn;
    }

    public String getCopyExpressCodeBtnText() {
        return copyExpressCodeBtnText;
    }

    public void setCopyExpressCodeBtnText(String copyExpressCodeBtnText) {
        this.copyExpressCodeBtnText = copyExpressCodeBtnText;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDiliverTimeDesc() {
        return diliverTimeDesc;
    }

    public void setDiliverTimeDesc(String diliverTimeDesc) {
        this.diliverTimeDesc = diliverTimeDesc;
    }

    public int getDiliverType() {
        return diliverType;
    }

    public void setDiliverType(int diliverType) {
        this.diliverType = diliverType;
    }

    public String getDiliverTypeName() {
        return diliverTypeName;
    }

    public void setDiliverTypeName(String diliverTypeName) {
        this.diliverTypeName = diliverTypeName;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public int getExpressId() {
        return expressId;
    }

    public void setExpressId(int expressId) {
        this.expressId = expressId;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerId() {
        return merId;
    }

    public void setMerId(int merId) {
        this.merId = merId;
    }

    public String getMerStoreAddress() {
        return merStoreAddress;
    }

    public void setMerStoreAddress(String merStoreAddress) {
        this.merStoreAddress = merStoreAddress;
    }

    public int getMerStoreId() {
        return merStoreId;
    }

    public void setMerStoreId(int merStoreId) {
        this.merStoreId = merStoreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOperateState() {
        return operateState;
    }

    public void setOperateState(int operateState) {
        this.operateState = operateState;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrgPrice() {
        return orgPrice;
    }

    public void setOrgPrice(int orgPrice) {
        this.orgPrice = orgPrice;
    }

    public int getPayBtn() {
        return payBtn;
    }

    public void setPayBtn(int payBtn) {
        this.payBtn = payBtn;
    }

    public String getPayBtnText() {
        return payBtnText;
    }

    public void setPayBtnText(String payBtnText) {
        this.payBtnText = payBtnText;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRecAddress() {
        return recAddress;
    }

    public void setRecAddress(String recAddress) {
        this.recAddress = recAddress;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecPhone() {
        return recPhone;
    }

    public void setRecPhone(String recPhone) {
        this.recPhone = recPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<OperateListBean> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<OperateListBean> operateList) {
        this.operateList = operateList;
    }

    public List<SubOrderInfoBean> getSubOrderInfo() {
        return subOrderInfo;
    }

    public void setSubOrderInfo(List<SubOrderInfoBean> subOrderInfo) {
        this.subOrderInfo = subOrderInfo;
    }

    public static class OperateListBean {
        /**
         * createTime : 2019-03-28T05:42:15.871Z
         * id : 0
         * name : string
         * remark : string
         */

        private String createTime;
        private int id;
        private String name;
        private String remark;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public static class SubOrderInfoBean {
        /**
         * id : 0
         * orderId : 0
         * prodCount : 0
         * prodId : 0
         * prodImg : string
         * prodName : string
         * prodPrice : 0
         * prodSpecId : 0
         * prodSpecName : string
         * totalPrice : string
         */

        private int id;
        private int orderId;
        private int prodCount;
        private int prodId;
        private String prodImg;
        private String prodName;
        private int prodPrice;
        private int prodSpecId;
        private String prodSpecName;
        private String totalPrice;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getProdCount() {
            return prodCount;
        }

        public void setProdCount(int prodCount) {
            this.prodCount = prodCount;
        }

        public int getProdId() {
            return prodId;
        }

        public void setProdId(int prodId) {
            this.prodId = prodId;
        }

        public String getProdImg() {
            return prodImg;
        }

        public void setProdImg(String prodImg) {
            this.prodImg = prodImg;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public int getProdPrice() {
            return prodPrice;
        }

        public void setProdPrice(int prodPrice) {
            this.prodPrice = prodPrice;
        }

        public int getProdSpecId() {
            return prodSpecId;
        }

        public void setProdSpecId(int prodSpecId) {
            this.prodSpecId = prodSpecId;
        }

        public String getProdSpecName() {
            return prodSpecName;
        }

        public void setProdSpecName(String prodSpecName) {
            this.prodSpecName = prodSpecName;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }
    }
}
