package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;

/**
 * Created by Alen on 2016/11/26.
 * 通用的title
 */
public class CommonTitleBar extends LinearLayout implements View.OnClickListener {
    private ImageView mLeftTextView;
    private ImageView mRightIconView;
    private ImageView mCenterImageView;
    private TextView mCenterTextView;
    private TextView mRightTextView;

    private OnClickListener mLeftClickListener;
    private OnClickListener mRightClickListener;

    public CommonTitleBar(Context context) {
        super(context);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.mLeftTextView = findViewById(R.id.m_common_title_bar_left_title);
        this.mCenterTextView = findViewById(R.id.m_common_title_bar_center_title);
        this.mRightIconView = findViewById(R.id.m_common_title_bar_right_title);
        this.mCenterImageView = findViewById(R.id.m_common_title_bar_center_icon);
        this.mRightTextView = findViewById(R.id.m_common_title_bar_right_text);

        this.mLeftTextView.setOnClickListener(this);
        this.mRightIconView.setOnClickListener(this);
        mRightTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_common_title_bar_left_title) {
            if (this.mLeftClickListener != null) this.mLeftClickListener.onClick(v);
        } else if (id == R.id.m_common_title_bar_right_title || id == R.id.m_common_title_bar_right_text) {
            if (this.mRightClickListener != null) this.mRightClickListener.onClick(v);
        }
    }

    public void setCenterText(String title) {
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText(title);
        this.mCenterImageView.setVisibility(View.GONE);
    }

    public void setRithgText(String title) {
        mRightTextView.setVisibility(View.VISIBLE);
        this.mRightIconView.setVisibility(View.GONE);
        this.mRightTextView.setText(title);
    }

    public void setCenterImage() {
        this.mCenterTextView.setVisibility(View.GONE);
        this.mCenterImageView.setVisibility(View.VISIBLE);
    }

    public void setLeftImage(int res) {
        this.mLeftTextView.setImageResource(res);
    }

    public void setRightImage(int res) {
        this.mRightIconView.setImageResource(res);
        this.mRightIconView.setVisibility(View.VISIBLE);
        mRightTextView.setVisibility(View.GONE);
    }

    public void hideRightImage() {
        this.mRightIconView.setVisibility(View.GONE);
    }

    public void setLeftClickListener(OnClickListener listener) {
        this.mLeftClickListener = listener;
    }

    public void setRightClickListener(OnClickListener listener) {
        this.mRightClickListener = listener;
    }

    public void setLeftImageVisible(int visible) {
        this.mLeftTextView.setVisibility(visible);
    }
}
