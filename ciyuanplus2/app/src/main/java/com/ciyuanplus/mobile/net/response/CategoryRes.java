package com.ciyuanplus.mobile.net.response;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019-04-08 20:05
 * class   : CategoryRes
 * desc   :
 * version: 1.0
 */
public class CategoryRes {

    private String code;
    private String msg;
    private List<BeanItem> mList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BeanItem> getData() {
        return mList;
    }

    public void setData(List<BeanItem> data) {
        this.mList = data;
    }

    public static class BeanItem {
        /**
         * id : 2
         * name : Cos服
         * parentId : 0
         * orderNum : 1
         * merId : 6
         */

        private int id;
        private String name;
        private int parentId;
        private int orderNum;
        private int merId;

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

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public int getMerId() {
            return merId;
        }

        public void setMerId(int merId) {
            this.merId = merId;
        }

        @Override
        public String toString() {
            return "item{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", parentId=" + parentId +
                    ", orderNum=" + orderNum +
                    ", merId=" + merId +
                    '}';
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (mList != null) {
            for (BeanItem item : mList) {
                sb.append("分类数据 ：" + item.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
