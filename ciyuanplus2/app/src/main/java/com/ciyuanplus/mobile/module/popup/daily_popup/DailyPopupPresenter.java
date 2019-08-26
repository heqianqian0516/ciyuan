package com.ciyuanplus.mobile.module.popup.daily_popup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.bean.DailyLabelInfo;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class DailyPopupPresenter implements DailyPopupContract.Presenter {
    DailyLabelInfo mDailyLabelInfo;
    private final DailyPopupContract.View mView;

    @Inject
    public DailyPopupPresenter(DailyPopupContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {
        mDailyLabelInfo = (DailyLabelInfo) intent.getSerializableExtra(Constants.INTENT_DAILY_LABEL);
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_DAILY_POP_UP_SHOW_TIME, mDailyLabelInfo.showDate);
    }

    // 返回保存的路径
    @Override
    public String createShareImage() {
        // 先获取截屏
        mView.getPopupLayout().setDrawingCacheEnabled(true);
        mView.getPopupLayout().buildDrawingCache();
        Bitmap bitmap = mView.getPopupLayout().getDrawingCache();
        Bitmap scancode = BitmapFactory.decodeResource(mView.getDefaultContext().getResources(), R.mipmap.share_daily_scan_code);
        Bitmap scanbitmap = Bitmap.createScaledBitmap(scancode, Utils.dip2px(50.4f), Utils.dip2px(64.8f), true);
        //添加水印
        Canvas cv = new Canvas(bitmap);
        cv.drawBitmap(scanbitmap, bitmap.getWidth() - Utils.dip2px(70), Utils.dip2px(23), null);
        cv.save();
        cv.restore();

        // 保存
        File f = new File(CacheManager.getInstance().getCacheDirectory() + System.currentTimeMillis() + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //
        return f.getAbsolutePath();
    }

    @Override
    public void detachView() {
    }


}
