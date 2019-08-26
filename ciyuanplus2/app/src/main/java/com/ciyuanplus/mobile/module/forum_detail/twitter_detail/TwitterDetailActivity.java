package com.ciyuanplus.mobile.module.forum_detail.twitter_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.ReportPostActivity;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.activity.news.SelectEditOrDeleteNewsActivity;
import com.ciyuanplus.mobile.adapter.NineGridViewClickAdapterImp;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.module.start_forum.start_twitter.StartTwitterActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.video.SampleCoverVideo;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;

/**
 * @author Alen
 * @date 2017/5/12
 * <p>
 * 以前的说说  类型
 */

public class TwitterDetailActivity extends ForumDetailActivity implements EventCenterManager.OnHandleEventListener {

    private ImageView mHeadIconImage;
    private ImageView mSexIconImage;
    private TextView mNameText;
    private TextView mTimeText;
    private TextView mContentText;
    private NineGridView mNineGrid;
    private TextView mFollowImage;
    private TextView mBrowseCount;
    private TextView mCommentCount;
    private TextView mLikeCount;
    SampleCoverVideo coverVideo;

    @Override
    protected void onResume() {
        super.onResume();
    }

    /** 更新界面信息 */
    @Override
    public void updateView() {
        if (null == mPresenter.mItem) {
            return;
        }
        super.updateView();
        mSexIconImage.setVisibility(View.VISIBLE);
        mNameText.setText(mPresenter.mItem.nickname);

        if (mPresenter.mItem.isAnonymous == 1) {
            mHeadIconImage.setImageResource(R.mipmap.default_head_);
            mSexIconImage.setVisibility(View.GONE);

        } else if (!Utils.isStringEmpty(mPresenter.mItem.photo)) {
            // 防止闪动
            if (!Utils.isStringEquals(mPresenter.mItem.photo, (String) mHeadIconImage.getTag(R.id.glide_item_tag))) {
                Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.photo)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                                .dontAnimate().centerCrop()).into(mHeadIconImage);
                mHeadIconImage.setTag(R.id.glide_item_tag, mPresenter.mItem.photo);
            }
        }

        mSexIconImage.setImageResource(mPresenter.mItem.getSexImageResource());
        if (Utils.isStringEmpty(mPresenter.mItem.description)) {
            mContentText.setVisibility(View.GONE);
        } else {
            mContentText.setVisibility(View.VISIBLE);
            mContentText.setText(mPresenter.mItem.description.replace("\r", "\n"));
        }
        mTimeText.setVisibility(View.GONE);
        mTimeText.setText(Utils.getFormattedTimeString(mPresenter.mItem.createTime));

        if (StringUtils.equals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mTitleBar.setRightImage(R.mipmap.nav_icon_more);
        } else {
            mTitleBar.setRightImage(R.drawable.icon_post_detail_report);
        }

        if (mPresenter.mItem.isAnonymous != 1 && mPresenter.mItem.isFollow != 1 && !Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {

            mFollowImage.setText(mPresenter.mItem.isFollow == 0 ? "+关注" : "已关注");
            mFollowImage.setVisibility(View.VISIBLE);
            mFollowImage.setOnClickListener(myOnClickListener);

        } else {
            mFollowImage.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(mPresenter.mItem.imgs)) {

            if (!StringUtils.isEmpty(mPresenter.mItem.postType) && StringUtils.equals(mPresenter.mItem.postType, "1")) {

                //视频帖子
                coverVideo.setVisibility(View.VISIBLE);
                mNineGrid.setVisibility(View.GONE);


                coverVideo.loadCoverImage(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.imgs, R.mipmap.imgfail);
                coverVideo.setUpLazy(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.imgs, true, null, null, "这是title");
                //增加title
                coverVideo.getTitleTextView().setVisibility(View.GONE);
                //设置返回键
                coverVideo.getBackButton().setVisibility(View.GONE);
                //设置全屏按键功能
                coverVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        coverVideo.startWindowFullscreen(TwitterDetailActivity.this, false, true);
                    }
                });

                //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
                coverVideo.setAutoFullWithSize(true);
                //音频焦点冲突时是否释放
                coverVideo.setReleaseWhenLossAudio(false);
                //全屏动画
                coverVideo.setShowFullAnimation(true);
                //小屏时不触摸滑动
                coverVideo.setIsTouchWiget(false);
                //全屏是否需要lock功能

            } else {
                coverVideo.setVisibility(View.GONE);
                mNineGrid.setVisibility(View.VISIBLE);

                ArrayList<ImageInfo> imageInfo = new ArrayList<>();

                String[] paths = mPresenter.mItem.imgs.split(",");
                for (String path : paths) {

                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(Constants.IMAGE_LOAD_HEADER + path);
                    info.setBigImageUrl(Constants.IMAGE_LOAD_HEADER + path);
                    imageInfo.add(info);

                }
                mNineGrid.setAdapter(new NineGridViewClickAdapterImp(mActivity, imageInfo, mPresenter.mItem));
            }
        }


        mBrowseCount.setText(Utils.formateNumber(mPresenter.mItem.browseCount));
        mCommentCount.setText(Utils.formateNumber(mPresenter.mItem.commentCount));
        mLikeCount.setText(Utils.formateNumber(mPresenter.mItem.likeCount));
        mCommonDetailBottomOperaLp1LikeImage.setImageResource(mPresenter.mItem.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
        Drawable drawable = getResources().getDrawable(mPresenter.mItem.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
        mLikeCount.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        mLikeCount.setOnClickListener(myOnClickListener);
    }

    @Override
    public void bindTopLayout() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headView = inflater != null ? inflater.inflate(R.layout.header_post_detail, null) : null;
        mCommentList.addHeaderView(headView);

        mNullCommentLayout = headView.findViewById(R.id.ll_empty_view);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyboardUtils.isSoftInputVisible(mActivity)) {
                    KeyboardUtils.hideSoftInput(mActivity);
                }
            }
        });

        mNameText = headView.findViewById(R.id.tv_name);

        mTimeText = headView.findViewById(R.id.tv_time);
        mSexIconImage = headView.findViewById(R.id.iv_sex_icon);
        mContentText = headView.findViewById(R.id.tv_content);
        mNineGrid = headView.findViewById(R.id.nineGrid);
        mFollowImage = headView.findViewById(R.id.tv_add);
        mHeadIconImage = headView.findViewById(R.id.riv_head_image);
        mBrowseCount = headView.findViewById(R.id.tv_browse_count);
        mCommentCount = headView.findViewById(R.id.tv_comment_count);
        mLikeCount = headView.findViewById(R.id.tv_like_count);
        mHeadIconImage.setOnClickListener(myOnClickListener);

        coverVideo = headView.findViewById(R.id.detail_player);

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);

//        mHeadIconImage.requestFocus();//解决进入页面会自动滑动的问题
    }

    // 打开 编辑的选择activity
    @Override
    public void showNewsOperaActivity(int type) {
        if (mPresenter.mItem == null) {
            return;
        }
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
                Intent intent = new Intent(TwitterDetailActivity.this, StartTwitterActivity.class);
                intent.putExtra(Constants.INTENT_UDPATE_NENWS_ITEM, mPresenter.mItem);
                startActivity(intent);
            } else if (Utils.isStringEquals(SelectEditOrDeleteNewsActivity.DELETE, opera)) {
                mPresenter.deleteFreshNews(mPresenter.mItem);
            } else if (Utils.isStringEquals(SelectEditOrDeleteNewsActivity.COLLECT, opera)) {
                if (mPresenter.mItem.isDislike == 1) {
                    mPresenter.cancelCollectPost();
                } else {
                    mPresenter.collectPost();
                }
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

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_add) {
                if (mPresenter.mItem == null) {
                    return;
                }
                if (!LoginStateManager.isLogin()) {

                    Intent intent = new Intent(TwitterDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (mPresenter.mItem.isFollow == 0) {
                    mPresenter.requestFollowUser();
                }

            } else if (id == R.id.riv_head_image) {
                if (mPresenter.mItem == null) {
                    return;
                }
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (mPresenter.mItem.isAnonymous == 1 || Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    return;
                }
                Intent intent = new Intent(TwitterDetailActivity.this, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, mPresenter.mItem.userUuid);
                startActivity(intent);
            } else if (id == R.id.tv_like_count) {

                if (mPresenter.mItem.isLike == 1) {
                    mPresenter.cancelLikePost();
                } else {
                    mPresenter.likePost();
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GSYVideoManager.releaseAllVideos();

        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM) {
            mPresenter.requestForumDetail();
        } else {
            Logger.d("EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT do nothing ");
        }
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }


}
