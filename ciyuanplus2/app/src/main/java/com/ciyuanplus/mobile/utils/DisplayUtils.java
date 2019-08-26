package com.ciyuanplus.mobile.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 小狼 on 2018/4/12.
 */

public class DisplayUtils {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            //不是第一个的格子都设一个左边和底部的间距
            int pos = parent.getChildLayoutPosition(view);
            if (pos % 2 == 1) {  //下面一行

                outRect.bottom = Utils.dip2px(10);
                outRect.top = Utils.dip2px(10);


            } else { //上面一行
                if (pos == 0) {
                    outRect.bottom = Utils.dip2px(10);
                } else {
                    outRect.top = Utils.dip2px(10);
                    outRect.bottom = Utils.dip2px(10);
                }


            }

        }
    }
}
