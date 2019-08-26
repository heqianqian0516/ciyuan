/**
  * Copyright 2019 bejson.com 
  */
package com.ciyuanplus.mobile.net.response;

/**
 * Auto-generated: 2019-04-09 2:31:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private int id;
    private String name;
    private int parentId;
    private int orderNum;
    private int merId;
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

    public void setParentId(int parentId) {
         this.parentId = parentId;
     }
     public int getParentId() {
         return parentId;
     }

    public void setOrderNum(int orderNum) {
         this.orderNum = orderNum;
     }
     public int getOrderNum() {
         return orderNum;
     }

    public void setMerId(int merId) {
         this.merId = merId;
     }
     public int getMerId() {
         return merId;
     }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                ", merId=" + merId +
                '}';
    }
}