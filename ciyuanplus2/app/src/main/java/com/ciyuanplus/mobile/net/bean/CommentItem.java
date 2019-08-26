package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class CommentItem implements Serializable {
    @SerializedName("commentUuid")
    public String commentUuid;//评论uuid , ,
    @SerializedName("contentText")
    public String contentText;//评论内容 ,
    @SerializedName("createTime")
    public String createTime;//创建时间 ,
    @SerializedName("identity")
    public int identity;//用户身份 ,
    @SerializedName("isPublish")
    public int isPublish;//冻结状态 ,
    @SerializedName("isResolved")
    public int isResolved;//是否解决
    @SerializedName("nickname")
    public String nickname;// 用户昵称 ,
    @SerializedName("photo")
    public String photo;//新鲜事发布用户头像
    @SerializedName("updateTime")
    public String updateTime;//新鲜事时间 ,
    @SerializedName("userUuid")
    public String userUuid;//新鲜事发布用户uuid
    @SerializedName("replyCommentResults")
    public ReplyItem[] replyCommentResults;//回复列表 ,
    @SerializedName("sex")
    public int sex;//用户性别 ,
    @SerializedName("currentCommunityUuid")
    public String currentCommunityUuid;// ,
    @SerializedName("isAnonymous")
    public int isAnonymous; // 是否需要匿名
    @SerializedName("isLike")
    public int isLike;//当前用户是否赞过(0:否，1:是) ,
    @SerializedName("likeCount")
    public int likeCount;//评论点赞数量 ,
    @SerializedName("score")
    public double score;//当前用户分数  ,
}
