package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.HorizontalScrollView;

/**
 * Created by Alen on 2017/11/24.
 * <p>
 * 单行 横向滚动  GridView
 */

public class HorizontalGridView extends HorizontalScrollView {
    private GridView mHorizontalGridView;

    public HorizontalGridView(Context context) {
        super(context);
    }

    public HorizontalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        setHorizontalScrollBarEnabled(false);
//
//        mHorizontalGridView = findViewById(R.id.m_horizontal_grid_view);
//
//    }
//
//    public void setAdapter(BaseAdapter adapter, int columWidth) {// 需要固定每个单元的宽度
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                adapter.getCount() * columWidth, LinearLayout.LayoutParams.FILL_PARENT);
//        mHorizontalGridView.setLayoutParams(params);// 设置GirdView布局参数
//        mHorizontalGridView.setStretchMode(GridView.NO_STRETCH);
//        mHorizontalGridView.setColumnWidth(columWidth);
//        mHorizontalGridView.setNumColumns(adapter.getCount());
//        mHorizontalGridView.setAdapter(adapter);
//    }
//
//    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
//        mHorizontalGridView.setOnItemClickListener(listener);
//    }
}
