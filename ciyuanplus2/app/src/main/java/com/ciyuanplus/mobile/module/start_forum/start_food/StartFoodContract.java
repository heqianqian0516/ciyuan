package com.ciyuanplus.mobile.module.start_forum.start_food;

import android.content.Intent;
import android.os.Bundle;

import com.ciyuanplus.mobile.module.BaseContract;
import com.sendtion.xrichtext.RichTextEditor;

/**
 * Created by Alen on 2017/12/11.
 */

class StartFoodContract {
    interface Presenter extends BaseContract.Presenter{
        void onActivityResult(int requestCode, int resultCode, Intent data);

        String getEditData();

        String[] getEditImages();

        void removeImage(String image);

        void initData(Intent intent, Bundle savedInstanceState);

        void saveInstanceState(Bundle outState);

        void submmit();
    }

    interface View extends BaseContract.View{
        void showEditData(String content);

        RichTextEditor getContextView();

        void showLoadingDialog();

        void dismissLoadingDialog();

        int getScore();

        void setScore(double score);

        void changePlaceName(String placeName);
    }
}
