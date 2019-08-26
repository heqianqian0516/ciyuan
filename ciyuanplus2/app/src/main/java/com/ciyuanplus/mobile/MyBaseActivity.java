package com.ciyuanplus.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.orhanobut.logger.Logger;
import com.umeng.message.PushAgent;

/**
 * Created by Alen on 2017/4/19.
 */

public class MyBaseActivity extends FragmentActivity {

    protected Activity mActivity;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.d("onCreate");
        mActivity = this;
        initLoadingView();
        PushAgent.getInstance(this).onAppStart(); //  用户消息推送 ， 必须要加
//        StatisticsManager.onEventInfo(this.getLocalClassName(), "onCreate");// 这里最好不要用 getClass().getSimpleName()，因为可能存在不同文件夹下面的相同类名

    }

    private void initLoadingView() {
        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("onResume");
//        MobclickAgent.onPageStart(getClass().getSimpleName());
//        // 用户Umeng 统计，
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("onPause");
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
//        // 用户Umeng 统计
//        MobclickAgent.onPause(this);
        hideKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        mActivity = null;
        // 防止 glide   java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity

        if (Util.isOnMainThread() && !this.isFinishing()) {
            Glide.with(this).pauseRequests();
        }
    }

    // 打点可以 直接调用这个函数， 如果需要在Activity 里面写 onClickListener 都要先调用这个方法。
    public void onViewClicked(View v) {
        StatisticsManager.onEventInfo(v.getContext().getClass().getName(),
                v.getResources().getResourceName(v.getId()), "OnClick");
    }

    protected void hideKeyboard() {
        KeyboardUtils.hideSoftInput(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Logger.d("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Logger.d("onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Logger.d("onStop");
    }

    protected void jumpToH5(String url) {
        Intent intent = new Intent(this, JsWebViewActivity.class);
        intent.putExtra(Constants.INTENT_OPEN_URL, url);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);

    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    protected void showToast(String content) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonToast.getInstance(content).show();
            }
        });
    }

    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }


}
