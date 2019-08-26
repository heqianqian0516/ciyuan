package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by yxm on 2017.02.15.
 */

public class PullRefreshLayout extends ViewGroup {
    private boolean isNeedRefresh;
    private View mHeader;
    private View mFooter;
    private TextView mHeaderText;
    private ImageView mHeadImage;
    private TextView mFooterText;
    private ImageView mFootVImage;
    private int mEffectiveHeaderHeight;
    private int mlastMoveY;
    private int mLastYIntercept;
    private int lastChildIndex;
    private AnimationDrawable headAnimationDrawable;
    private AnimationDrawable footAnimationDrawable;
    private int mLostMoveX;
    private final Scroller mScoller;
    private boolean mEnableRefresh = true;
    private boolean mEnableLoadMore = true;
    private Status mStatus = Status.NORMAL;
    private OnRefreshListener mRefreshListener;
    private final android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            startHeadAnim();
        }
    };
    private OnPullListener onPullListener;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScoller = new Scroller(context);
    }

    public void setmEnableRefresh(boolean mEnableRefresh) {
        this.mEnableRefresh = mEnableRefresh;
    }

    public void setmEnableLoadMore(boolean mEnableLoadMore) {
        this.mEnableLoadMore = mEnableLoadMore;
    }

    private void updateStatus(Status status) {
        mStatus = status;
    }

    public void setRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    // 当view的所有child从xml中被初始化后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lastChildIndex = getChildCount() - 1;
        addHeader();
        addFooter();
    }

    private void addHeader() {
        mHeader = LayoutInflater.from(getContext()).inflate(R.layout.pull_header, null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mHeader, params);

        mHeaderText = findViewById(R.id.header_text);
        mHeadImage = findViewById(R.id.iv_head);

        setHeadFrameAnimDrawable();
    }

    private void addFooter() {
        mFooter = LayoutInflater.from(getContext()).inflate(R.layout.home_pull_footer, null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mFooter, params);
        mFooterText = findViewById(R.id.footer_text);
//        mFooterProgressBar = (ProgressBar) findViewById(R.id.footer_progressbar);
        mFootVImage = findViewById(R.id.iv_foot);

        setFootFrameAnimDrawable();
    }

    private void setFootFrameAnimDrawable() {

        // 把动画资源设置为imageView的背景,也可直接在XML里面设置
        mFootVImage.setImageResource(R.drawable.loading_loadmore_amin_list);
        footAnimationDrawable = (AnimationDrawable) mFootVImage.getDrawable();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutContentHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == mHeader) {
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
                mEffectiveHeaderHeight = child.getHeight();
            } else if (child == mFooter) {
                child.layout(0, layoutContentHeight, child.getMeasuredWidth(), layoutContentHeight + child.getMeasuredHeight());

            } else {
                child.layout(0, layoutContentHeight, child.getMeasuredWidth(), layoutContentHeight + child.getMeasuredHeight());
                if (i < getChildCount()) {
                    if (child instanceof ScrollView) {
                        layoutContentHeight += getMeasuredHeight();
                        continue;
                    }
                    layoutContentHeight += child.getMeasuredHeight();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();

        // 正在刷新或加载更多，避免重复
        if (mStatus == Status.REFRESHING || mStatus == Status.LOADING) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mlastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:

                int dy = mlastMoveY - y;

                // 一直在下拉
                if (getScrollY() <= 0 && dy <= 0 && mEnableRefresh) {
                    if (mStatus == Status.TRY_LOADMORE) {
                        scrollBy(0, dy / 30);
                    } else {
                        scrollBy(0, dy / 3);
                    }
                }
                // 一直在上拉
                else if (getScrollY() >= 0 && dy >= 0 && mEnableLoadMore) {
                    if (mStatus == Status.TRY_REFRESH) {
                        scrollBy(0, dy / 30);
                    } else {
                        scrollBy(0, dy / 3);
                    }
                } else {
                    scrollBy(0, dy / 3);
                }

                beforeRefreshing(dy);
                beforeLoadMore();

                break;
            case MotionEvent.ACTION_UP:
                // 下拉刷新，并且到达有效长度
                if (getScrollY() <= -mEffectiveHeaderHeight * 1.5) {

                    if (isNeedRefresh) {
                        releaseWithStatusRefresh();
                        if (mRefreshListener != null) {
                            mRefreshListener.onRefresh();
                        }
                    } else {
                        if (null != onPullListener) {
                            onPullListener.onPull();
                        }

//                        releaseWithStatusTryRefresh();
                    }
                } else if (getScrollY() <= -mEffectiveHeaderHeight) {
                    releaseWithStatusRefresh();
                    if (mRefreshListener != null) {
                        mRefreshListener.onRefresh();
                    }
                }
                // 上拉加载更多，达到有效长度
                else if (getScrollY() >= Utils.dip2px(80)) {
                    releaseWithStatusLoadMore();
                    if (mRefreshListener != null) {
                        mRefreshListener.onLoadMore();
                    }
                } else {
                    releaseWithStatusTryRefresh();
                    releaseWithStatusTryLoadMore();
                }
                break;
        }

        mlastMoveY = y;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        int y = (int) event.getY();
        int x = (int) event.getX();
        if (mStatus == Status.REFRESHING || mStatus == Status.LOADING) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // 拦截时需要记录点击位置，不然下一次滑动会出错
                mLostMoveX = x;
                mlastMoveY = y;
                intercept = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                float slopX = Math.abs(x - mLostMoveX);
                float slopY = Math.abs(y - mlastMoveY);
                //  Log.log("slopX=" + slopX + ", slopY="  + slopY);
                if ((slopX > 0 || slopY > 0) && slopX > slopY) {
                    intercept = false;
                } else {
                    if (y > mLastYIntercept) {
                        View child = getChildAt(0);
                        intercept = getRefreshIntercept(child);

                        if (intercept) {
                            updateStatus(Status.TRY_REFRESH);
                        }
                    } else if (y < mLastYIntercept) {
                        View child = getChildAt(lastChildIndex);
                        intercept = getLoadMoreIntercept(child);

                        if (intercept) {
                            updateStatus(Status.TRY_LOADMORE);
                        }
                    } else {
                        intercept = false;
                    }
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                intercept = false;
                break;
            }
        }

        mLastYIntercept = y;
        return intercept;
    }

    /*汇总判断 刷新和加载是否拦截*/
    private boolean getRefreshIntercept(View child) {
        boolean intercept = false;

        if (child instanceof AdapterView) {
            intercept = adapterViewRefreshIntercept(child);
        } else if (child instanceof ScrollView) {
            intercept = scrollViewRefreshIntercept(child);
        } else if (child instanceof RecyclerView) {
            intercept = recyclerViewRefreshIntercept(child);
        }
        return intercept;
    }

    private boolean getLoadMoreIntercept(View child) {
        boolean intercept = false;

        if (child instanceof AdapterView) {
            intercept = adapterViewLoadMoreIntercept(child);
        } else if (child instanceof ScrollView) {
            intercept = scrollViewLoadMoreIntercept(child);
        } else if (child instanceof RecyclerView) {
            intercept = recyclerViewLoadMoreIntercept(child);
        }
        return intercept;
    }

    /*具体判断各种View是否应该拦截*/
    // 判断AdapterView下拉刷新是否拦截
    private boolean adapterViewRefreshIntercept(View child) {
        boolean intercept = true;
        AdapterView adapterChild = (AdapterView) child;
        if (adapterChild.getFirstVisiblePosition() != 0
                || adapterChild.getChildAt(0).getTop() != 0) {
            intercept = false;
        }
        return intercept;
    }
    /*汇总判断 刷新和加载是否拦截*/

    // 判断AdapterView加载更多是否拦截
    private boolean adapterViewLoadMoreIntercept(View child) {
        boolean intercept = false;
        AdapterView adapterChild = (AdapterView) child;
        if (adapterChild.getLastVisiblePosition() == adapterChild.getCount() - 1 &&
                (adapterChild.getChildAt(adapterChild.getChildCount() - 1).getBottom() >= getMeasuredHeight())) {
            intercept = true;
        }
        return intercept;
    }

    // 判断ScrollView刷新是否拦截
    private boolean scrollViewRefreshIntercept(View child) {
        boolean intercept = false;
        if (child.getScrollY() <= 0) {
            intercept = true;
        }
        return intercept;
    }

    // 判断ScrollView加载更多是否拦截
    private boolean scrollViewLoadMoreIntercept(View child) {
        boolean intercept = false;
        ScrollView scrollView = (ScrollView) child;
        View scrollChild = scrollView.getChildAt(0);

        if (scrollView.getScrollY() >= (scrollChild.getHeight() - scrollView.getHeight())) {
            intercept = true;
        }
        return intercept;
    }

    // 判断RecyclerView刷新是否拦截
    private boolean recyclerViewRefreshIntercept(View child) {
        boolean intercept = false;

        RecyclerView recyclerView = (RecyclerView) child;
        if (recyclerView.computeVerticalScrollOffset() <= 0) {
            intercept = true;
        }
        return intercept;
    }

    // 判断RecyclerView加载更多是否拦截
    private boolean recyclerViewLoadMoreIntercept(View child) {
        boolean intercept = false;

        RecyclerView recyclerView = (RecyclerView) child;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange()) {
            intercept = true;
        }

        return intercept;
    }

    private void setHeadFrameAnimDrawable() {

        // 把动画资源设置为imageView的背景,也可直接在XML里面设置
        mHeadImage.setImageResource(R.drawable.loading_head_amin_list);
        headAnimationDrawable = (AnimationDrawable) mHeadImage.getDrawable();
    }
    /*具体判断各种View是否应该拦截*/

    //
//    private void setFootFrameAnimDrawable(){
//        mFootImage.setImageResource(R.drawable.loading_loadmore_amin_list);
//        footAnimationDrawable = (AnimationDrawable) mFootImage.getDrawable();
//    }
    private void startHeadAnim() {
        if (headAnimationDrawable != null && !headAnimationDrawable.isRunning()) {
            handler.post(() -> headAnimationDrawable.start());
        }
    }

    private void finishHeadAnim() {
        if (headAnimationDrawable != null && headAnimationDrawable.isRunning()) {
            handler.post(() -> headAnimationDrawable.stop());

        }
    }

    private void startFootAnim() {
        if (footAnimationDrawable != null && !footAnimationDrawable.isRunning()) {
            handler.post(() -> footAnimationDrawable.start());
        }
    }

    private void finishFootAnim() {
        if (footAnimationDrawable != null && footAnimationDrawable.isRunning()) {
            handler.post(() -> footAnimationDrawable.stop());

        }
    }

    /*修改header和footer的状态*/
    private void beforeRefreshing(float dy) {
        //计算旋转角度
        int scrollY = Math.abs(getScrollY());
        //        float angle = (float) (scrollY * 1.0 / mEffectiveHeaderHeight * 180);
//        mHeaderArrow.setRotation(angle);


        if (getScrollY() <= -mEffectiveHeaderHeight * 1.5) {
//            mHeaderText.setText("下拉有惊喜");

        } else if (getScrollY() <= -mEffectiveHeaderHeight) {
            mHeaderText.setText("松开刷新");

        } else {
            mHeaderText.setText("下拉刷新");

        }
    }

    private void beforeLoadMore() {
        if (getScrollY() >= Utils.dip2px(80)) {
            mFooterText.setText("松开加载更多");
        } else {
            mFooterText.setText("上拉加载更多");
        }
    }

    public void refreshFinished() {
//        scrollTo(0, 0);
        mScoller.startScroll(0, getScrollY(), 0, -getScrollY(), 1000);

//        mHeaderProgressBar.setVisibility(GONE);
//        mHeaderArrow.setVisibility(VISIBLE);
        finishHeadAnim();
        mHeaderText.setText("下拉刷新");
        updateStatus(Status.NORMAL);
    }

    public void loadMoreFinished() {
        finishFootAnim();

//        mFooterProgressBar.setVisibility(GONE);
//        scrollTo(0, 0);
        mScoller.startScroll(0, getScrollY(), 0, -getScrollY(), 1000);
        mFooterText.setText("上拉加载");
        updateStatus(Status.NORMAL);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScoller.computeScrollOffset()) {
            scrollTo(0, mScoller.getCurrY());
        }
        postInvalidate();
    }

    public void releaseWithStatusTryRefresh() {
        scrollBy(0, -getScrollY());
        mHeaderText.setText("下拉刷新");
        updateStatus(Status.NORMAL);


    }

    private void releaseWithStatusTryLoadMore() {
        scrollBy(0, -getScrollY());
        mFooterText.setText("上拉加载更多");
        updateStatus(Status.NORMAL);
    }

    private void releaseWithStatusRefresh() {
        scrollTo(0, -mEffectiveHeaderHeight);
//        mHeaderProgressBar.setVisibility(VISIBLE);
//        mHeaderArrow.setVisibility(GONE);
        mHeaderText.setText("正在刷新");
        updateStatus(Status.REFRESHING);
        startHeadAnim();
    }

    private void releaseWithStatusLoadMore() {
        scrollTo(0, Utils.dip2px(80));
        mFooterText.setText("正在加载");
//        mFooterProgressBar.setVisibility(VISIBLE);
        startFootAnim();
        updateStatus(Status.LOADING);
    }

    public void setOnPullListener(OnPullListener onPullListener) {
        this.onPullListener = onPullListener;
    }


    private enum Status {
        NORMAL, TRY_REFRESH, REFRESHING, TRY_LOADMORE, LOADING
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface OnPullListener {
        void onPull();
    }
}
