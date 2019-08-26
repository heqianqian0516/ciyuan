package com.ciyuanplus.mobile.widget.smartRefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.header.MaterialHeader;

/**
 * Created by kk on 2018/5/3.
 */

public class WalkRefreshHeader extends MaterialHeader {
    public WalkRefreshHeader(Context context) {
        super(context);
    }

    public WalkRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WalkRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//
//    private ImageView mAnimImage;
//    private TextView mStateText;
//
//    public WalkRefreshHeader(Context context) {
//        this(context, null);
//    }
//
//    public WalkRefreshHeader(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public WalkRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        initView(context);
//    }
//
//    private void initView(Context context) {
//
//
//        View view = View.inflate(context, R.layout.pull_header, this);
//        mAnimImage = view.findViewById(R.id.iv_head);
//        mStateText = view.findViewById(R.id.header_text);
//        mStateText.setVisibility(GONE);
//    }
//
//    @NonNull
//    @Override
//    public View getView() {
//        return this;
//    }
//
//    @NonNull
//    @Override
//    public SpinnerStyle getSpinnerStyle() {
//        return SpinnerStyle.Translate;
//    }
//
//    @Override
//    public void setPrimaryColors(int... colors) {
//
//    }
//
//    @Override
//    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//
//    }
//
//
//    @Override
//    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
//        return 0;
//    }
//
//    @Override
//    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
//
//    }
//
//    @Override
//    public boolean isSupportHorizontalDrag() {
//        return false;
//    }
//
//    @Override
//    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
//
//        switch (newState) {
//            case PullDownToRefresh: //下拉刷新开始。正在下拉还没松手时调用
//                //每次重新下拉时，将图片资源重置为小人的大脑袋
//                mAnimImage.setImageResource(R.drawable.load_more_anim_0008);
//                mStateText.setText("下拉刷新");
//                break;
//            case Refreshing: //正在刷新。只调用一次
//                //状态切换为正在刷新状态时，设置图片资源为小人卖萌的动画并开始执行
//                mAnimImage.setImageResource(R.drawable.loading_loadmore_amin_list);
//                AnimationDrawable refreshingAnim = (AnimationDrawable) mAnimImage.getDrawable();
//                refreshingAnim.start();
//                mStateText.setText("正在刷新");
//                break;
//            case ReleaseToRefresh:
//                //状态切换为正在刷新状态时，设置图片资源为小人卖萌的动画并开始执行
//                mAnimImage.setImageResource(R.drawable.load_more_anim_0008);
//                mStateText.setText("释放刷新");
//                break;
//
//        }
//    }
//

}
