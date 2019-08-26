package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;

/**
 * Created by Alen on 2017/12/29.
 * 重叠的 头像图片
 * <p>
 * 最多5张，最少一张
 */

public class DuplicateHeadIconImageView extends RelativeLayout {

    private final ImageView[] imageViews = new ImageView[5];

    public DuplicateHeadIconImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_duplicate_head_icon, this);
        initView();
    }

    private void initView() {
        imageViews[0] = findViewById(R.id.m_view_duplicate_head_icon_1);
        imageViews[1] = findViewById(R.id.m_view_duplicate_head_icon_2);
        imageViews[2] = findViewById(R.id.m_view_duplicate_head_icon_3);
        imageViews[3] = findViewById(R.id.m_view_duplicate_head_icon_4);
        imageViews[4] = findViewById(R.id.m_view_duplicate_head_icon_5);
    }

    public void setDataSource(String[] images) {
        if (images == null || images.length == 0) return;
        imageViews[0].setVisibility(View.VISIBLE);
        imageViews[1].setVisibility(images.length >= 2 ? View.VISIBLE : View.GONE);
        imageViews[2].setVisibility(images.length >= 3 ? View.VISIBLE : View.GONE);
        imageViews[3].setVisibility(images.length >= 4 ? View.VISIBLE : View.GONE);
        imageViews[4].setVisibility(images.length >= 5 ? View.VISIBLE : View.GONE);

        for (int i = 0; i < images.length && i < 5; i++) {
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + images[i])
                    .apply(new RequestOptions().dontAnimate().centerCrop()).into(imageViews[i]);
        }
    }
}
