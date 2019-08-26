package com.ciyuanplus.mobile.net.bean;

public class RankedListBean {


    /**
     * code : 1
     * msg : 操作成功
     * data : {"postUuid":"97370F17F0BD4D63A79C2440B1D4CCCC","contentText":"（vhbyvrnyn）","communityUuid":"F5C4F2FDC0F8488C953DC2E0AB4C1031","communityName":"不贰过蒸汽烟俱乐部","imgs":"a01dffd3ffaea198850d5643442be295.jpg,8482489e3080a694ed2ebcfcfc04c281.jpg,61e6e0970db2658b591c177ae163fa93.jpg,d8991135a8742e3331b85a6ff7ff2dfd.jpg,4f76381a98a5b1f53f36af61f288b7c8.jpg,88386e31b499e4c3de54d9585aec7879.jpg,d597277e5e4567e80960aaf530641803.jpg,7e6296f9edcb54f4073a290a32e4cba1.jpg,cdf30db38e2884b82112551d5ff55b2e.jpg","likeCount":1800,"dislikeCount":0,"commentCount":0,"browseCount":1113,"isAnonymous":0,"userUuid":"9E58A232255D41418AD7E71E3A32CB30","nickname":"A柠檬甜甜圈A","photo":"bd7cf42fd5443b5afbcf977fb4a73f23.jpg","sex":null,"currentCommunityName":null,"currentCommunityUuid":"F5C4F2FDC0F8488C953DC2E0AB4C1031","isLike":0,"isDislike":0,"isFollow":0,"updateTime":1562295539000,"createTime":1562295539000,"title":null,"bizType":0,"renderType":1,"id":877,"description":"（vhbyvrnyn）","postType":0,"someOne":null,"someTwo":null,"someThree":null,"postScore":null,"placeName":null,"placeScore":null,"isRated":0,"placeAddress":null,"longitude":null,"latitude":null,"isResolved":0,"isHighlight":0,"isActivity":null,"activityUuid":"189aa81ff4064cbb87ddbfef923a1c4b","price":0,"rank":null}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * postUuid : 97370F17F0BD4D63A79C2440B1D4CCCC
         * contentText : （vhbyvrnyn）
         * communityUuid : F5C4F2FDC0F8488C953DC2E0AB4C1031
         * communityName : 不贰过蒸汽烟俱乐部
         * imgs : a01dffd3ffaea198850d5643442be295.jpg,8482489e3080a694ed2ebcfcfc04c281.jpg,61e6e0970db2658b591c177ae163fa93.jpg,d8991135a8742e3331b85a6ff7ff2dfd.jpg,4f76381a98a5b1f53f36af61f288b7c8.jpg,88386e31b499e4c3de54d9585aec7879.jpg,d597277e5e4567e80960aaf530641803.jpg,7e6296f9edcb54f4073a290a32e4cba1.jpg,cdf30db38e2884b82112551d5ff55b2e.jpg
         * likeCount : 1800
         * dislikeCount : 0
         * commentCount : 0
         * browseCount : 1113
         * isAnonymous : 0
         * userUuid : 9E58A232255D41418AD7E71E3A32CB30
         * nickname : A柠檬甜甜圈A
         * photo : bd7cf42fd5443b5afbcf977fb4a73f23.jpg
         * sex : null
         * currentCommunityName : null
         * currentCommunityUuid : F5C4F2FDC0F8488C953DC2E0AB4C1031
         * isLike : 0
         * isDislike : 0
         * isFollow : 0
         * updateTime : 1562295539000
         * createTime : 1562295539000
         * title : null
         * bizType : 0
         * renderType : 1
         * id : 877
         * description : （vhbyvrnyn）
         * postType : 0
         * someOne : null
         * someTwo : null
         * someThree : null
         * postScore : null
         * placeName : null
         * placeScore : null
         * isRated : 0
         * placeAddress : null
         * longitude : null
         * latitude : null
         * isResolved : 0
         * isHighlight : 0
         * isActivity : null
         * activityUuid : 189aa81ff4064cbb87ddbfef923a1c4b
         * price : 0
         * rank : null
         */

        private String postUuid;
        private String contentText;
        private String communityUuid;
        private String communityName;
        private String imgs;
        private int likeCount;
        private int dislikeCount;
        private int commentCount;
        private int browseCount;
        private int isAnonymous;
        private String userUuid;
        private String nickname;
        private String photo;
        private Object sex;
        private Object currentCommunityName;
        private String currentCommunityUuid;
        private int isLike;
        private int isDislike;
        private int isFollow;
        private long updateTime;
        private long createTime;
        private Object title;
        private int bizType;
        private int renderType;
        private int id;
        private String description;
        private int postType;
        private Object someOne;
        private Object someTwo;
        private Object someThree;
        private Object postScore;
        private Object placeName;
        private Object placeScore;
        private int isRated;
        private Object placeAddress;
        private Object longitude;
        private Object latitude;
        private int isResolved;
        private int isHighlight;
        private Object isActivity;
        private String activityUuid;
        private int price;
        private Object rank;

        public String getPostUuid() {
            return postUuid;
        }

        public void setPostUuid(String postUuid) {
            this.postUuid = postUuid;
        }

        public String getContentText() {
            return contentText;
        }

        public void setContentText(String contentText) {
            this.contentText = contentText;
        }

        public String getCommunityUuid() {
            return communityUuid;
        }

        public void setCommunityUuid(String communityUuid) {
            this.communityUuid = communityUuid;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(int dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getBrowseCount() {
            return browseCount;
        }

        public void setBrowseCount(int browseCount) {
            this.browseCount = browseCount;
        }

        public int getIsAnonymous() {
            return isAnonymous;
        }

        public void setIsAnonymous(int isAnonymous) {
            this.isAnonymous = isAnonymous;
        }

        public String getUserUuid() {
            return userUuid;
        }

        public void setUserUuid(String userUuid) {
            this.userUuid = userUuid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
        }

        public Object getCurrentCommunityName() {
            return currentCommunityName;
        }

        public void setCurrentCommunityName(Object currentCommunityName) {
            this.currentCommunityName = currentCommunityName;
        }

        public String getCurrentCommunityUuid() {
            return currentCommunityUuid;
        }

        public void setCurrentCommunityUuid(String currentCommunityUuid) {
            this.currentCommunityUuid = currentCommunityUuid;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public int getIsDislike() {
            return isDislike;
        }

        public void setIsDislike(int isDislike) {
            this.isDislike = isDislike;
        }

        public int getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(int isFollow) {
            this.isFollow = isFollow;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public int getBizType() {
            return bizType;
        }

        public void setBizType(int bizType) {
            this.bizType = bizType;
        }

        public int getRenderType() {
            return renderType;
        }

        public void setRenderType(int renderType) {
            this.renderType = renderType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPostType() {
            return postType;
        }

        public void setPostType(int postType) {
            this.postType = postType;
        }

        public Object getSomeOne() {
            return someOne;
        }

        public void setSomeOne(Object someOne) {
            this.someOne = someOne;
        }

        public Object getSomeTwo() {
            return someTwo;
        }

        public void setSomeTwo(Object someTwo) {
            this.someTwo = someTwo;
        }

        public Object getSomeThree() {
            return someThree;
        }

        public void setSomeThree(Object someThree) {
            this.someThree = someThree;
        }

        public Object getPostScore() {
            return postScore;
        }

        public void setPostScore(Object postScore) {
            this.postScore = postScore;
        }

        public Object getPlaceName() {
            return placeName;
        }

        public void setPlaceName(Object placeName) {
            this.placeName = placeName;
        }

        public Object getPlaceScore() {
            return placeScore;
        }

        public void setPlaceScore(Object placeScore) {
            this.placeScore = placeScore;
        }

        public int getIsRated() {
            return isRated;
        }

        public void setIsRated(int isRated) {
            this.isRated = isRated;
        }

        public Object getPlaceAddress() {
            return placeAddress;
        }

        public void setPlaceAddress(Object placeAddress) {
            this.placeAddress = placeAddress;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public int getIsResolved() {
            return isResolved;
        }

        public void setIsResolved(int isResolved) {
            this.isResolved = isResolved;
        }

        public int getIsHighlight() {
            return isHighlight;
        }

        public void setIsHighlight(int isHighlight) {
            this.isHighlight = isHighlight;
        }

        public Object getIsActivity() {
            return isActivity;
        }

        public void setIsActivity(Object isActivity) {
            this.isActivity = isActivity;
        }

        public String getActivityUuid() {
            return activityUuid;
        }

        public void setActivityUuid(String activityUuid) {
            this.activityUuid = activityUuid;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public Object getRank() {
            return rank;
        }

        public void setRank(Object rank) {
            this.rank = rank;
        }
    }
}
