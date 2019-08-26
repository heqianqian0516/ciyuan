/**
  * Copyright 2019 bejson.com 
  */
package com.ciyuanplus.mobile.net.response;
import java.util.List;

/**
 * Auto-generated: 2019-04-09 2:31:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CategoryResponse {

    private String code;
    private String msg;
    private List<Data> data;
    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setData(List<Data> data) {
         this.data = data;
     }
     public List<Data> getData() {
         return data;
     }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (data!=null){

            for (Data item:data) {
                stringBuilder.append("item  ： ").append(item.toString()).append("\n");
            }
        }
        return "CategoryResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + stringBuilder.toString() +
                '}';
    }
}