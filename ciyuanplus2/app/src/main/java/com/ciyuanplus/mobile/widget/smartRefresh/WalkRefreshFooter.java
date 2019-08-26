package com.ciyuanplus.mobile.widget.smartRefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

/**
 * Created by kk on 2018/5/3.
 */

public class WalkRefreshFooter extends BallPulseFooter {
    public WalkRefreshFooter(Context context) {
        super(context);
    }

    public WalkRefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WalkRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    private ImageView mFootImage;
//    private TextView mStateText;
//
//    public WalkRefreshFooter(Context context) {
//        this(context, null);
//    }
//
//    public WalkRefreshFooter(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public WalkRefreshFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        initView(context);
//    }
//
//    private void initView(Context context) {
//
//        View view = View.inflate(context, R.layout.pull_footer, this);
//        mFootImage = view.findViewById(R.id.iv_foot);
//        mStateText = view.findViewById(R.id.footer_text);
//    }
//
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
//            case PullUpToLoad: //上拉加载
//
//                mFootImage.setImageResource(R.drawable.load_more_anim_0008);
//                mStateText.setText("上拉加载");
//                break;
//            case Loading: //正在加载
//                mFootImage.setImageResource(R.drawable.loading_loadmore_amin_list);
//                AnimationDrawable refreshingAnim = (AnimationDrawable) mFootImage.getDrawable();
//                refreshingAnim.start();
//                mStateText.setText("正在加载");
//                break;
//            case ReleaseToRefresh:
//                //状态切换为正在刷新状态时，设置图片资源为小人卖萌的动画并开始执行
//                mFootImage.setImageResource(R.drawable.load_more_anim_0008);
//                mStateText.setText("释放加载");
//                break;
//
//        }
//    }
//
//
//    @Override
//    public boolean setNoMoreData(boolean noMoreData) {
//        return false;
//    }
}
