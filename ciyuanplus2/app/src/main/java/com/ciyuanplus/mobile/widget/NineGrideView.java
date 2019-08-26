package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.adapter.NineGridViewAdapter;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

/**
 * Created by Alen on 2017/1/7.
 * <p>
 * 9 图
 */
public class NineGrideView extends LinearLayout {
    private SleftAdapterGrideView mGridView;
    private SquareImageView mSingleImage;
    private final Context mContext;
    private final int mImageWidth = Utils.px2dip(Utils.getScreenWidth());

    public NineGrideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_nine_gride, this);
        initView();
    }

    private void initView() {
        mGridView = findViewById(R.id.m_nine_grid_view_gride);
        mSingleImage = findViewById(R.id.m_nine_grid_view_single_image);

//        int screenWidth = Utils.getScreenWidth() - Utils.dip2px(40);// 获取屏幕宽度  
//        ViewGroup.LayoutParams lp = mSingleImage.getLayoutParams();
//        lp.width = screenWidth;
//        lp.height = screenWidth / 16 * 9;
//        mSingleImage.setLayoutParams(lp);
    }

    public void setDataSource(final String[] images) {
        if (images == null || images.length == 0) return;
        if (images.length == 1) {//只有一張 显示大图
            mGridView.setVisibility(View.GONE);
            mSingleImage.setVisibility(View.VISIBLE);
            mSingleImage.setOnClickListener(v -> {
                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, images);
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, 0);
                App.mContext.startActivity(intent);
            });
//            mSingleImage.post(new Runnable() {
//                @Override
//                public void run() {
//                    ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + images[0], mSingleImage);
//                }
//            });
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + images[0] + Constants.IMAGE_LOAD_THUMB_END + ",w_" + mImageWidth + ",h_" + mImageWidth).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                    .dontAnimate().centerCrop()).into(mSingleImage);
//            ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + images[0], mSingleImage, new ImageSize(500,500));
        } else {
            mGridView.setVisibility(View.VISIBLE);
            mSingleImage.setVisibility(View.GONE);
            if (images.length == 2 || images.length == 4) mGridView.setNumColumns(2);
            else mGridView.setNumColumns(3);
            NineGridViewAdapter nineGridAdapter = new NineGridViewAdapter(mContext, images);
            mGridView.setAdapter(nineGridAdapter);
            nineGridAdapter.notifyDataSetChanged();
            mGridView.setOnItemClickListener((parent, view, position, id) -> {
                if (id == -1) {
                    return;
                }
                int postion = (int) id;
                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, images);
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, postion);
                App.mContext.startActivity(intent);
            });
        }
    }
}
