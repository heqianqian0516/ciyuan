package com.ciyuanplus.mobile.module.start_forum.start_news;

import android.content.Intent;
import android.os.Bundle;

import com.ciyuanplus.mobile.module.BaseContract;
import com.sendtion.xrichtext.RichTextEditor;

/**
 * Created by Alen on 2017/12/11.
 */

class StartNewsContract {
    interface Presenter extends BaseContract.Presenter{
        void initData(Intent intent, Bundle savedInstanceState);

        void saveInstanceState(Bundle outState);

        void submmit();// 提交按钮的响应函数

        String getEditData();

        void removeImage(String image);

        void dealActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface View extends BaseContract.View{
        String getTitleString();

        void dismissLoadingDialog();

        RichTextEditor getContentView();

        void updateView();

        void showLoadingDialog();

        boolean isAnonyChecked();
    }
}
