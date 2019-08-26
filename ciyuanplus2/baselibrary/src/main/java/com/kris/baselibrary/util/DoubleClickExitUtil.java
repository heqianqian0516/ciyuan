package com.kris.baselibrary.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;



/**
 * 双击退出
 */
public class DoubleClickExitUtil {

    private final Activity mActivity;
    private boolean isOnKeyBacking;
    private Handler mHandler;
    private Toast mBackToast;
    private String hint;

    public DoubleClickExitUtil(Activity activity) {
        mActivity = activity;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public DoubleClickExitUtil(Activity activity, String hint) {
        mActivity = activity;
        this.hint = hint;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mBackToast != null) {
                mBackToast.cancel();
            }
            mActivity.finish();
            return true;
        } else {
            isOnKeyBacking = true;
            if (mBackToast == null) {
                if (TextUtils.isEmpty(hint)) {
                    mBackToast = Toast.makeText(mActivity, "再按一次返回键退出应用", Toast.LENGTH_LONG);
                } else {
                    mBackToast = Toast.makeText(mActivity, hint, Toast.LENGTH_LONG);

                }
            }
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            isOnKeyBacking = false;
            if (mBackToast != null) {
                mBackToast.cancel();
            }
        }
    };
}
