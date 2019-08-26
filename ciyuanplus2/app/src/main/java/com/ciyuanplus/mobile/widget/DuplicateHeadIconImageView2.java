package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;

/**
 * Created by Alen on 2017/12/29.
 * 重叠的 头像图片
 * <p>
 * 最多10张，最少一张
 * 超过10张，显示More 图标
 */

public class DuplicateHeadIconImageView2 extends RelativeLayout {

    private final ImageView[] imageViews = new ImageView[10];
    private ImageView moreImage;

    public DuplicateHeadIconImageView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_duplicate_head_icon2, this);
        initView();
    }

    private void initView() {
        imageViews[0] = findViewById(R.id.m_view_duplicate_head_icon_1);
        imageViews[1] = findViewById(R.id.m_view_duplicate_head_icon_2);
        imageViews[2] = findViewById(R.id.m_view_duplicate_head_icon_3);
        imageViews[3] = findViewById(R.id.m_view_duplicate_head_icon_4);
        imageViews[4] = findViewById(R.id.m_view_duplicate_head_icon_5);
        imageViews[5] = findViewById(R.id.m_view_duplicate_head_icon_6);
        imageViews[6] = findViewById(R.id.m_view_duplicate_head_icon_7);
        imageViews[7] = findViewById(R.id.m_view_duplicate_head_icon_8);
        imageViews[8] = findViewById(R.id.m_view_duplicate_head_icon_9);
        imageViews[9] = findViewById(R.id.m_view_duplicate_head_icon_10);
        moreImage = findViewById(R.id.m_view_duplicate_more);
    }

    public void setDataSource(String[] images) {
        if (images == null || images.length == 0) return;
        imageViews[0].setVisibility(View.VISIBLE);
        imageViews[1].setVisibility(images.length >= 2 ? View.VISIBLE : View.GONE);
        imageViews[2].setVisibility(images.length >= 3 ? View.VISIBLE : View.GONE);
        imageViews[3].setVisibility(images.length >= 4 ? View.VISIBLE : View.GONE);
        imageViews[4].setVisibility(images.length >= 5 ? View.VISIBLE : View.GONE);
        imageViews[5].setVisibility(images.length >= 6 ? View.VISIBLE : View.GONE);
        imageViews[6].setVisibility(images.length >= 7 ? View.VISIBLE : View.GONE);
        imageViews[7].setVisibility(images.length >= 8 ? View.VISIBLE : View.GONE);
        imageViews[8].setVisibility(images.length >= 9 ? View.VISIBLE : View.GONE);
        imageViews[9].setVisibility(images.length >= 10 ? View.VISIBLE : View.GONE);
        moreImage.setVisibility(images.length > 10 ? View.VISIBLE : View.GONE);
        for (int i = 0; i < images.length && i < 10; i++) {
            Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + images[i])
                    .apply(new RequestOptions().dontAnimate().centerCrop()).into(imageViews[i]);
        }
    }
}
