package com.ciyuanplus.mobile.activity.news;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.FullScreenPreViewAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.CustomViewPager;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by Alen on 2017/1/8.
 */
public class FullScreenImageActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.m_full_screen_image_viewer)
    CustomViewPager mFullScreenImageViewer;
    @BindView(R.id.m_full_screen_indicator)
    TextView mFullScreenIndicator;
    private String[] mImages;
    //    private IndicatorView mIndicator;
    private FullScreenPreViewAdapter previewAdapter;
    private int mIndex;
    private String mPreActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title));
        mImages = getIntent().getExtras().getStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES);
        if (mImages == null || mImages.length == 0) return;
        mIndex = getIntent().getIntExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, 0);
        this.setContentView(R.layout.activity_full_screen_image_viewer);
        ButterKnife.bind(this);
        mFullScreenImageViewer.addOnPageChangeListener(this);
        previewAdapter = new FullScreenPreViewAdapter(mImages, this);
        mFullScreenImageViewer.setAdapter(previewAdapter);
        mFullScreenImageViewer.setCurrentItem(mIndex);
        mFullScreenIndicator.setText((mIndex + 1) + "/" + mImages.length);
        this.mFullScreenImageViewer.setOnClickListener(myOnClickListener);
        initData();
    }

    private void initData() {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mFullScreenIndicator.setText((position + 1) + "/" + mImages.length);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            int id = view.getId();
            if (id == R.id.m_full_screen_image_viewer) {
                FullScreenImageActivity.this.finish();
            }
        }
    };

   
}
