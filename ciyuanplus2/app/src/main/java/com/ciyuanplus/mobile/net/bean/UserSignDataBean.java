package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserSignDataBean implements Serializable {

    /**
     * code : 1
     * msg : 操作成功
     * data : [{"date":"2019-10-01"},{"date":"2019-10-02"},{"date":"2019-10-03"},{"date":"2019-10-04"},{"date":"2019-10-05"},{"date":"2019-10-06"},{"date":"2019-10-07"},{"date":"2019-10-08"},{"date":"2019-10-09"},{"date":"2019-10-10"},{"date":"2019-10-11"},{"date":"2019-10-12"},{"date":"2019-10-13"},{"date":"2019-10-14"},{"date":"2019-10-15"},{"date":"2019-10-16"},{"date":"2019-10-17"},{"date":"2019-10-18"},{"date":"2019-10-19"},{"date":"2019-10-20"},{"date":"2019-10-21","id":252},{"date":"2019-10-22"},{"date":"2019-10-23"},{"date":"2019-10-24"},{"date":"2019-10-25"},{"date":"2019-10-26"},{"date":"2019-10-27"},{"date":"2019-10-28"},{"date":"2019-10-29"},{"date":"2019-10-30"},{"date":"2019-10-31"}]
     */

    private String code;
    private String msg;
    @SerializedName("data")
    private List<DataBean> dataX;

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

    public List<DataBean> getDataX() {
        return dataX;
    }

    public void setDataX(List<DataBean> dataX) {
        this.dataX = dataX;
    }

    public static class DataBean {
        /**
         * date : 2019-10-01
         * id : 252
         */

        private String date;
        private int id;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
