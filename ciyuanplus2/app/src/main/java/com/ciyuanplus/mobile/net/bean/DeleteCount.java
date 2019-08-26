package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 删除回复或评论条目数
 * Created by kk on 2018/5/16.
 */

public class DeleteCount {

    @SerializedName("postUUID")
    public final String postUUID;
    @SerializedName("deleteCount")
    public final int deleteCount;

    public DeleteCount(String postUUID, int deleteCount) {

        this.postUUID = postUUID;
        this.deleteCount = deleteCount;
    }

}
