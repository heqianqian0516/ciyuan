package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.annotation.Nullable;

/**
 * Created by Alen on 2017/12/29.
 * 评分视图
 * 一共十分   5个星星
 */

public class MarkView extends LinearLayout implements View.OnClickListener {

    private final ImageView[] imageViews = new ImageView[5];
    private double mValue = 0;
    private boolean disable;// 默认可以点击
    private boolean editMode = false;// 默认可以点击
    private final int contentSize;
    private final int paddingLeft;

    public MarkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_mark, this);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarkViewStyle);
        contentSize = typedArray.getDimensionPixelSize(R.styleable.MarkViewStyle_contentSize, Utils.sp2px(getContext(), 19));
        paddingLeft = typedArray.getDimensionPixelSize(R.styleable.MarkViewStyle_leftPadding, Utils.sp2px(getContext(), 4));
        disable = typedArray.getBoolean(R.styleable.MarkViewStyle_disabled, false);

        initView();

        typedArray.recycle();
    }

    private void initView() {
        imageViews[0] = findViewById(R.id.m_view_mark_start_1);
        imageViews[1] = findViewById(R.id.m_view_mark_start_2);
        imageViews[2] = findViewById(R.id.m_view_mark_start_3);
        imageViews[3] = findViewById(R.id.m_view_mark_start_4);
        imageViews[4] = findViewById(R.id.m_view_mark_start_5);
        for (int i = 0; i < 5; i++) {
            ViewGroup.LayoutParams paramses = imageViews[i].getLayoutParams();
            paramses.width = contentSize;
            paramses.height = contentSize;
            imageViews[i].setPadding(paddingLeft, 0, 0, 0);
            imageViews[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (editMode) {
            CommonToast.getInstance("编辑时不能更改").show();
            return;
        }
        if (disable) return;
        int id = v.getId();
        if (id == R.id.m_view_mark_start_1) {
            mValue = 2.0f;
            changeViewState();
        } else if (id == R.id.m_view_mark_start_2) {
            mValue = 4.0f;
            changeViewState();
        } else if (id == R.id.m_view_mark_start_3) {
            mValue = 6.0f;
            changeViewState();
        } else if (id == R.id.m_view_mark_start_4) {
            mValue = 8.0f;
            changeViewState();
        } else if (id == R.id.m_view_mark_start_5) {
            mValue = 10.0f;
            changeViewState();
        }
    }

    private void changeViewState() {
        for (int i = 0; i < 5; i++) {
            if ((i + 1) * 2 <= mValue) {
                imageViews[i].setImageResource(R.mipmap.img_star_full);
            } else if (i * 2 < mValue) {
                imageViews[i].setImageResource(R.mipmap.img_star_halffull);
            } else {
                imageViews[i].setImageResource(R.mipmap.img_star_empty);
            }
        }
    }

    public void setEditMode() {
        editMode = true;
        setDisable(true);
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double score) {
        mValue = (int) score;
        changeViewState();
    }
}
