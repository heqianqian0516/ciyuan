package com.ciyuanplus.mobile.module.start_forum.start_stuff;

import android.content.Intent;
import android.os.Bundle;

import com.ciyuanplus.mobile.module.BaseContract;
import com.sendtion.xrichtext.RichTextEditor;

/**
 * Created by Alen on 2017/12/11.
 */

class StartStuffContract {
    interface Presenter extends BaseContract.Presenter{
        void initData(Intent intent, Bundle savedInstanceState);

        void submmit();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        String getEditData();

        void removeImage(String image);

        void saveInstanceState(Bundle outState);

        String[] getEditImages();
    }

    interface View extends BaseContract.View{
        String getTitleString();

        void dismissLoadingDialog();

        RichTextEditor getContextView();

        boolean getPayCheckState();

        double getPrice();

        void showLoadingDialog();

        void showEditData(String content);

        void updateView();

        void setZoneName(String commName);

        String getPriceString();
    }
}
