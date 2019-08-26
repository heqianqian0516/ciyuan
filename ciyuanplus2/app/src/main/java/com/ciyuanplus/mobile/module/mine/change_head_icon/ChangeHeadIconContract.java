package com.ciyuanplus.mobile.module.mine.change_head_icon;

import android.content.Intent;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class ChangeHeadIconContract {
    interface Presenter extends BaseContract.Presenter {
        void handleActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface View extends BaseContract.View {

        void showLoadingDialog();

        void dismissLoadingDialog();
    }
}
