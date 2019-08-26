package com.ciyuanplus.mobile.net.bean;

import java.util.List;

public class HomeADBean {

    /**
     * code : 1
     * msg : 操作成功
     * data : [{"uuid":"90D7BA83220A4B2990AAB2B3FD44C359","imgs":"edf286bb5248ad035ad421bbb990457c.jpeg","activityName":"test","activityContent":"sssssssssss","startTime":"2018-06-27 04:17:59","endTime":"2018-07-27 04:18:03","createTime":"2019-06-27 04:18:17","creator":"admin","modifyTime":null,"modifier":null,"isPublish":1,"isDel":0,"isPublic":null}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uuid : 90D7BA83220A4B2990AAB2B3FD44C359
         * imgs : edf286bb5248ad035ad421bbb990457c.jpeg
         * activityName : test
         * activityContent : sssssssssss
         * startTime : 2018-06-27 04:17:59
         * endTime : 2018-07-27 04:18:03
         * createTime : 2019-06-27 04:18:17
         * creator : admin
         * modifyTime : null
         * modifier : null
         * isPublish : 1
         * isDel : 0
         * isPublic : null
         */

        private String uuid;
        private String imgs;
        private String activityName;
        private String activityContent;
        private String startTime;
        private String endTime;
        private String createTime;
        private String creator;
        private String modifyTime;
        private String modifier;
        private int isPublish;
        private int isDel;
        private int isPublic;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getActivityContent() {
            return activityContent;
        }

        public void setActivityContent(String activityContent) {
            this.activityContent = activityContent;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getModifier() {
            return modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public int getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(int isPublish) {
            this.isPublish = isPublish;
        }

        public int getIsDel() {
            return isDel;
        }

        public void setIsDel(int isDel) {
            this.isDel = isDel;
        }

        public int getIsPublic() {
            return isPublic;
        }

        public void setIsPublic(int isPublic) {
            this.isPublic = isPublic;
        }
    }
}
