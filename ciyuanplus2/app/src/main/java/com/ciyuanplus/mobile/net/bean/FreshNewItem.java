package com.ciyuanplus.mobile.net.bean;

import com.ciyuanplus.mobile.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class FreshNewItem implements Serializable {
    public static final int FRESH_ITEM_POST = 0;// 以前的说说和长文
    public static final int FRESH_ITEM_STUFF = 1;// 宝贝
    public static final int FRESH_ITEM_ACTIVITY = 2;// 活动
    public static final int FRESH_ITEM_DAILY = 3;// 日签
    public static final int FRESH_ITEM_ANSWER = 4;// 问答
    public static final int FRESH_ITEM_NEWS = 5;// 新闻
    public static final int FRESH_ITEM_LIVE = 6;// 品质生活
    public static final int FRESH_ITEM_FOOD = 7;// 美食
    public static final int FRESH_ITEM_NOTE = 8;// 生活随手记
    public static final int FRESH_ITEM_COMMENT = 12;// 邻里点评
    public static final int FRESH_ITEM_NEWS_COLLECTION = 13;//新闻合集
    @SerializedName("followType")
    public int followType;//关注类型 0-被关注|1-我关注对方|2-互相关注 ,
    @SerializedName("contentText")
    public String contentText;//新鲜事内容 ,
    @SerializedName("communityUuid")
    public String communityUuid;//用户发帖所在小区uuid
    @SerializedName("communityName")
    public String communityName;//用户发帖所在小区uuid
    @SerializedName("currentCommunityName")
    public String currentCommunityName;//用户当前所在小区名称
    @SerializedName("currentCommunityUuid")
    public String currentCommunityUuid;//用户当前所在小区uuid
    @SerializedName("imgs")
    public String imgs;//新鲜事图片
    @SerializedName("isAnonymous")
    public int isAnonymous;//新鲜事图片
    @SerializedName("nickname")
    public String nickname;//新鲜事发布用户昵称
    @SerializedName("photo")
    public String photo;//新鲜事发布用户头像
    @SerializedName("updateTime")
    public String updateTime;//新鲜事时间 ,
    @SerializedName("createTime")
    public String createTime;//新鲜事发布时间 ,
    @SerializedName("userUuid")
    public String userUuid;//新鲜事发布用户uuid
    @SerializedName("postUuid")
    public String postUuid;//新鲜事UUID
    @SerializedName("commentCount")
    public int commentCount;//新鲜事评论量
    @SerializedName("dislikeCount")
    public int dislikeCount;//新鲜事踩数量 ,
    @SerializedName("isDislike")
    public int isDislike;//当前用户是否踩过(0:否，1:是) ,
    @SerializedName("isLike")
    public int isLike;//当前用户是否赞过(0:否，1:是) ,
    @SerializedName("likeCount")
    public int likeCount;//新鲜事赞数量 ,
    @SerializedName("isFollow")
    public int isFollow;//新鲜事赞数量 ,
    @SerializedName("isPublic")
    public int isPublic;//是否公开到世界 ,
    @SerializedName("sex")
    private int sex;//性别 ,
    @SerializedName("browseCount")
    public int browseCount;//浏览量
    @SerializedName("title")
    public String title;//浏览量
    @SerializedName("bizType")
    public int bizType;//业务类型(0:帖子，1:宝贝 2 banner  3:日签)
    @SerializedName("renderType")
    public int renderType;//
    @SerializedName("description")
    public String description;//描述
    @SerializedName("id")
    public String id;//id ,
    @SerializedName("postType")
    public String postType;//新鲜事分类
    @SerializedName("price")
    public double price;//
    @SerializedName("status")
    public int status;
    @SerializedName("distance")
    public double distance;//
    @SerializedName("someOne")
    public String someOne;//日签 图片作者 uuid
    @SerializedName("someTwo")
    public String someTwo;//日签  图片作者 名字
    @SerializedName("someThree")
    public String someThree;////日签  图片 作者 头像
    @SerializedName("isRated")
    public int isRated;////是否已点评
    @SerializedName("placeName")
    public String placeName;////地点名称 ,]
    @SerializedName("placeId")
    public String placeId;////地点Id
    @SerializedName("placeScore")
    public double placeScore;////地点评分 ,
    @SerializedName("postScore")
    public double postScore;////帖子评分 ,
    @SerializedName("wishPhotos")
    public String[] wishPhotos;////想吃用户头像列表
    @SerializedName("ratePhotos")
    public String[] ratePhotos;////吃过用户头像列表
    @SerializedName("placeAddress")
    public String placeAddress;////地点详细地址
    @SerializedName("longitude")
    public String longitude;////
    @SerializedName("latitude")
    public String latitude;////
    @SerializedName("isResolved")//isResolved (integer, optional): 是否解决 ,
    public int isResolved;
    @SerializedName("isTop")
    public int isTop;////


    public String getStatus() {
        String statusString = "在售中";
        if (status == 6) {
        }
        else if (status == 7) statusString = "已预订";
        else if (status == 8) statusString = "已售出";
        return statusString;
    }

    // 获取性别图片
    public int getSexImageResource() {
        if (sex == UserInfoItem.SEX_NONE) return 0;
        return sex == UserInfoItem.SEX_MAN ?
                R.mipmap.my_icon_boy_small : R.mipmap.my_icon_girl_small;
    }

    // 获取性别图片
    public int getBigSexImageResource() {
        if (sex == UserInfoItem.SEX_NONE) return 0;
        return sex == UserInfoItem.SEX_MAN ?
                R.mipmap.my_icon_boy : R.mipmap.my_icon_girl;
    }
}
