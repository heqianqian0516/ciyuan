package com.ciyuanplus.mobile.module.start_forum.start_seek_help;

import android.content.Intent;
import android.os.Bundle;

import com.ciyuanplus.mobile.module.BaseContract;
import com.sendtion.xrichtext.RichTextEditor;

/**
 * Created by Alen on 2017/12/11.
 */

class StartSeekHelpContract {
    interface Presenter extends BaseContract.Presenter{
        void initData(Intent intent, Bundle savedInstanceState);// 初始化数据

        void saveInstanceState(Bundle outState); // 保存instances

        void requestPost(String s, int biztype);// 发布或者提交编辑

        void removeImage(String image); // 点击删除图片

        String[] getEditImages(RichTextEditor postContent);

        void dealActivityResult(int requestCode, int resultCode, Intent data, RichTextEditor postContent);
    }

    interface View extends BaseContract.View{
        void updateView();// 更新页面内容

        void dismissLoadingDialog();

        void showLoadingDialog();

        String getEditData();

        boolean isAnonyChecked();// 判断匿名是否选中
    }
}
