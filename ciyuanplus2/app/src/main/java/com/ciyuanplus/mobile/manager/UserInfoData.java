package com.ciyuanplus.mobile.manager;

import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.GsonUtils;

/**
 * Created by Alen on 2017/2/11.
 * <p>
 * 管理登陆的用户信息
 * 保存在一个常亮 和本地数据中
 */
public class UserInfoData {
    private static UserInfoData sUserInfoData;
    private final UserInfoItem mUserInfoItem;


    private UserInfoData() {
        mUserInfoItem = queryLocalUserItem();
    }

    public static synchronized UserInfoData getInstance() {
        if (sUserInfoData == null) {
            sUserInfoData = new UserInfoData();
        }
        return sUserInfoData;
    }

    public UserInfoItem getUserInfoItem() {
        return mUserInfoItem;
    }

    public void clearUserInfo() {
        sUserInfoData = null;
    }

    public long insertOrReplace(String id, String json) {
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_USRER_INFO, json);
        sUserInfoData = new UserInfoData();
        return 0;
    }

    private UserInfoItem queryLocalUserItem() {

        String infoJson = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_USRER_INFO, "");
        UserInfoItem userInfoItem = GsonUtils.getGsson().fromJson(infoJson, UserInfoItem.class);
        return userInfoItem;
    }
}
