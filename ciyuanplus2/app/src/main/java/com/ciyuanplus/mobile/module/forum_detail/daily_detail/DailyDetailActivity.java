package com.ciyuanplus.mobile.module.forum_detail.daily_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.ReportPostActivity;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.activity.news.SelectEditOrDeleteNewsActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;

import androidx.annotation.Nullable;

/**
 * Created by Alen on 2017/12/15.
 */

public class DailyDetailActivity extends ForumDetailActivity {
    private TextView day;
    private TextView time;
    private TextView week;
    private TextView month;
    private ImageView head;
    private ImageView image;
    private TextView user;
    private TextView content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void bindTopLayout() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeadView = inflater != null ? inflater.inflate(R.layout.activity_daily_detail_layout, null) : null;
        mCommentList.addHeaderView(mHeadView);

        mNullCommentLayout = mHeadView.findViewById( R.id.m_daily_detail_null_lp);
        day = mHeadView.findViewById( R.id.m_daily_detail_day);
        time = mHeadView.findViewById( R.id.m_daily_detail_time);
        week = mHeadView.findViewById( R.id.m_daily_detail_week);
        month = mHeadView.findViewById( R.id.m_daily_detail_month);
        head = mHeadView.findViewById( R.id.m_daily_detail_head);
        image = mHeadView.findViewById( R.id.m_daily_detail_image);
        user = mHeadView.findViewById( R.id.m_daily_detail_user_name);
        content = mHeadView.findViewById( R.id.m_daily_detail_content);

        head.setOnClickListener(myOnClickListener);
    }

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if(view.getId() == R.id.m_daily_detail_head) {
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (mPresenter.mItem.isAnonymous == 1 || Utils.isStringEquals(mPresenter.mItem.someOne, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(DailyDetailActivity.this, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, mPresenter.mItem.someOne);
                DailyDetailActivity.this.startActivity(intent);
            }
        }
    };

    @Override
    public void updateView() {
        if (mPresenter.mItem == null) return;
        super.updateView();
        time.setText(Utils.getFormattedTimeString(mPresenter.mItem.createTime));
        String[] result = Utils.formateDate(Long.parseLong(mPresenter.mItem.createTime));
        day.setText(result[2]);
        month.setText(result[0] + "/" + result[1]);
        week.setText(result[3]);
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.someThree)
                .apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop()).into(head);
        user.setText("摄影: " + mPresenter.mItem.someTwo);
        content.setText(mPresenter.mItem.contentText);
        if (Utils.isStringEmpty(mPresenter.mItem.imgs)) {
            image.setVisibility(View.GONE);
        } else{
            String[] images = mPresenter.mItem.imgs.split(",");
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + images[0]).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                    .dontAnimate().centerCrop()).into(image);
        }
    }

    // 打开 编辑的选择activity
    @Override
    public void showNewsOperaActivity(int type) {
        if (mPresenter.mItem == null) return;
        Intent intent = new Intent(this, SelectEditOrDeleteNewsActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, type);
        intent.putExtra(Constants.INTENT_POST_HAS_COLLECTED, mPresenter.mItem.isDislike);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_EDIT_DELETE_NEWS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_EDIT_DELETE_NEWS && resultCode == RESULT_OK) {
            String opera = data.getStringExtra(SelectEditOrDeleteNewsActivity.SELECTED);
            if (Utils.isStringEquals(SelectEditOrDeleteNewsActivity.EDIT, opera)) {
                CommonToast.getInstance("请从后台编辑该日签").show();
            } else if (Utils.isStringEquals(SelectEditOrDeleteNewsActivity.DELETE, opera)) {
                mPresenter.deleteFreshNews(mPresenter.mItem);
            } else if (Utils.isStringEquals(SelectEditOrDeleteNewsActivity.COLLECT, opera)) {
                if (mPresenter.mItem.isDislike == 1) mPresenter.cancelCollectPost();
                else mPresenter.collectPost();
            } else if (Utils.isStringEquals(SelectEditOrDeleteNewsActivity.REPORT, opera)) {
                Intent intent = new Intent(this, ReportPostActivity.class);
                intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, 0);
                intent.putExtra(Constants.INTENT_POST_ID, mPresenter.mItem.postUuid);
                startActivity(intent);
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_DELETE_COMMENT && resultCode == RESULT_OK) {
            String opera = data.getStringExtra(SelectDeleteCommentActivity.SELECTED);
            if (Utils.isStringEquals(SelectDeleteCommentActivity.DELETE_COMENT, opera)) {
                mPresenter.deleteComment(mPresenter.mTryDeleteComment);
            } else if (Utils.isStringEquals(SelectDeleteCommentActivity.DELETE_REPLY, opera)) {
                mPresenter.deleteReply(mPresenter.mTryDeleteReply);
            }
        }
    }
}
