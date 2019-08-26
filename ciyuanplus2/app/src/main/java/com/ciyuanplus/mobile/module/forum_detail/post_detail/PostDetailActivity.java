package com.ciyuanplus.mobile.module.forum_detail.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.activity.news.ReportPostActivity;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.activity.news.SelectEditOrDeleteNewsActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.PostTypeManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.module.start_forum.start_post.StartPostActivity;
import com.ciyuanplus.mobile.module.start_forum.start_seek_help.StartSeekHelpActivity;
import com.ciyuanplus.mobile.net.bean.PostTypeItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.sendtion.xrichtext.RichTextView;
import com.sendtion.xrichtext.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/5/12.
 * 长文和  bannber详情页面
 */

public class PostDetailActivity extends ForumDetailActivity implements EventCenterManager.OnHandleEventListener {

    private TextView mBannerTimeText;
    private ImageView mHeadIconImage;
    private LinearLayout typeLayout;
    private ImageView typeIcon;
    private TextView typeName;
    private ImageView mSexIconImage;
    private ImageView mNeighborImage;
    private TextView mNameText;
    private TextView mTimeText;
    private TextView mTitleText;
    private ImageView mFollowImage;
    private RichTextView mContentView;

    private final ArrayList<String> mImageList = new ArrayList<>();
    RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_head_).error(R.mipmap.default_head_).dontAnimate().centerCrop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // 更新界面信息
    @Override
    public void updateView() {

        if (mPresenter.mItem == null) return;
        super.updateView();
        if (mPresenter.mItem.bizType == 14) {
            //每日精选不显示三个圆圈
            mTitleBar.setRightImageVisible(View.GONE);
        }
        mSexIconImage.setVisibility(View.VISIBLE);
        mNameText.setText(mPresenter.mItem.nickname);
        if (Utils.isStringEquals(mPresenter.mItem.currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid) && !Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mNeighborImage.setVisibility(View.VISIBLE);
        } else {
            mNeighborImage.setVisibility(View.INVISIBLE);
        }
        if (mPresenter.mItem.isAnonymous == 1) {
            mHeadIconImage.setImageResource(R.mipmap.default_head_);
            mSexIconImage.setVisibility(View.GONE);
            mNameText.setText("匿名");
            mNeighborImage.setVisibility(View.INVISIBLE);
        } else if (!Utils.isStringEmpty(mPresenter.mItem.photo)) {
            // 防止闪动
            if (!Utils.isStringEquals(mPresenter.mItem.photo, (String) mHeadIconImage.getTag(R.id.glide_item_tag))) {
                Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.photo).apply(options).into(mHeadIconImage);
            }
            mHeadIconImage.setTag(R.id.glide_item_tag, mPresenter.mItem.photo);

//            mCoummunityName.setVisibility(View.VISIBLE);

        }
//        mCoummunityName.setText(mPresenter.mItem.currentCommunityName);
        mSexIconImage.setImageResource(mPresenter.mItem.getSexImageResource());

        mTimeText.setText(Utils.getFormattedTimeString(mPresenter.mItem.createTime));
        mBannerTimeText.setText(Utils.getFormattedTimeString(mPresenter.mItem.createTime));
        if (!Utils.isStringEmpty(mPresenter.mItem.description))
            mTitleText.setText(mPresenter.mItem.description.replace("\r", "\n"));

        showContentData(mPresenter.mItem.contentText);

        if (mPresenter.mItem.isAnonymous != 1 && mPresenter.mItem.isFollow != 1 && !Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mFollowImage.setVisibility(View.VISIBLE);
            mFollowImage.setOnClickListener(myOnClickListener);
        } else {
            mFollowImage.setVisibility(View.GONE);
        }

//        if(!Utils.isStringEmpty(mPresenter.mItem.imgs)){
//            if(!Utils.isStringEquals(mPresenter.mItem.imgs, (String) mNineGrid.getTag())){// 防止闪动
//                mNineGrid.setTag(mPresenter.mItem.imgs);
//                mNineGrid.setDataSource(mPresenter.mItem.imgs.split(","));
//            }
//        }
//        else mNineGrid.setVisibility(View.GONE);

        if (mPresenter.isBanner) {
            findViewById(R.id.m_news_detail_top_lp).setVisibility(View.GONE);
            findViewById(R.id.m_news_detail_banner_lp).setVisibility(View.VISIBLE);
            findViewById(R.id.m_news_detail_time_lp).setVisibility(View.GONE);
        }
        //V2.1.0 添加标签
        PostTypeItem postTypeItem = PostTypeManager.getInstance().getPostType(mPresenter.mItem.postType);
        if (mPresenter.mHideTag || Utils.isStringEmpty(mPresenter.mItem.postType) || postTypeItem == null) {
            // 如果没有对应的类型， 不显示这个标签。
            typeLayout.setVisibility(View.GONE);
        } else {
            typeLayout.setVisibility(View.GONE);
            typeName.setText(postTypeItem.typeName);
            typeLayout.setTag(postTypeItem);
            typeLayout.setOnClickListener(myOnClickListener);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_head_).error(R.mipmap.default_head_).dontAnimate().centerCrop();
            Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + postTypeItem.icon).apply(options).into(typeIcon);
        }
    }

    @Override
    public void bindTopLayout() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeadView = inflater != null ? inflater.inflate(R.layout.activity_post_detail_layout, null) : null;
        mCommentList.addHeaderView(mHeadView);

        mNullCommentLayout = mHeadView.findViewById(R.id.m_news_detail_null_lp);
        typeLayout = mHeadView.findViewById(R.id.m_news_detail_news_type_lp);
        typeIcon = mHeadView.findViewById(R.id.m_news_detail_type_image);
        typeName = mHeadView.findViewById(R.id.m_news_detail_type_name);

        mHeadIconImage = mHeadView.findViewById(R.id.riv_head_image);
        mNameText = mHeadView.findViewById(R.id.tv_name);
        mNeighborImage = mHeadView.findViewById(R.id.m_news_detail_neighbor_image);
        mTimeText = mHeadView.findViewById(R.id.tv_time);
        mSexIconImage = mHeadView.findViewById(R.id.iv_sex_icon);
        mTitleText = mHeadView.findViewById(R.id.m_news_detail_title_text);
        mContentView = mHeadView.findViewById(R.id.m_news_detail_content);
        mFollowImage = mHeadView.findViewById(R.id.m_news_detail_follow_text);
//        mCoummunityName = mHeadView.findViewById( R.id.m_news_detail_community_name);
        mHeadIconImage.setOnClickListener(myOnClickListener);
        // V2.1.0 添加Banner详情分类
        mBannerTimeText = mHeadView.findViewById(R.id.m_news_detail_banner_time_text);

        mContentView.setmImagePreViewListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                int pos = mContentView.getImageViewIndex(view);
                String[] imges = new String[mImageList.size()];
                for (int i = 0; i < mImageList.size(); i++) {
                    imges[i] = mImageList.get(i);
                }
                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, imges);
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, pos);
                PostDetailActivity.this.startActivity(intent);
            }
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);

//        mHeadIconImage.requestFocus();//解决进入页面会自动滑动的问题
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
                if (mPresenter.mItem.bizType == 4) {
                    Intent intent = new Intent(PostDetailActivity.this, StartSeekHelpActivity.class);
                    intent.putExtra(Constants.INTENT_UDPATE_NENWS_ITEM, mPresenter.mItem);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PostDetailActivity.this, StartPostActivity.class);
                    intent.putExtra(Constants.INTENT_UDPATE_NENWS_ITEM, mPresenter.mItem);
                    startActivity(intent);
                }

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

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            int id = view.getId();
            if (id == R.id.m_news_detail_follow_text) {
                if (mPresenter.mItem == null) return;
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(PostDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                mPresenter.requestFollowUser();
            } else if (id == R.id.riv_head_image) {
                if (mPresenter.mItem == null) return;
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (mPresenter.mItem.isAnonymous == 1 || Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(PostDetailActivity.this, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, mPresenter.mItem.userUuid);
                startActivity(intent);
            }
        }
    };

    private void showContentData(final String content) {
        mContentView.post(() -> {
            mContentView.clearAllLayout();
            mImageList.clear();
            List<String> textList = StringUtils.cutStringByImgTag(content);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    String imagePath = StringUtils.getImgSrc(text);
                    mImageList.add(imagePath);

//                        int width = Utils.getScreenWidth();
//                        int height = Utils.getScreenHeight();
                    mContentView.measure(0, 0);
//                        Bitmap bitmap = ImageUtils.getSmallBitmap(Constants.IMAGE_LOAD_HEADER  + imagePath, width, height);
//                        if (bitmap != null){
//                            mContentView.addImageViewAtIndex(mContentView.getLastIndex(), Constants.IMAGE_LOAD_HEADER + imagePath);
//                        } else {
//                            mContentView.addTextViewAtIndex(mContentView.getLastIndex(), text);
//                        }
                    mContentView.addImageViewAtIndex(mContentView.getLastIndex(), Constants.IMAGE_LOAD_HEADER + imagePath);

//                        mContentView.addTextViewAtIndex(mContentView.getLastIndex(), text);
                } else {
                    mContentView.addTextViewAtIndex(mContentView.getLastIndex(), text);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM) {
            mPresenter.requestForumDetail();
        }
    }

    // 测试方法
    @JavascriptInterface
    public void showImage(String imageUrl, int idx, int imageCount, String[] imageUrlArray) {

        runOnUiThread(() -> {
            Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
            Bundle b = new Bundle();
            b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, imageUrlArray);
            intent.putExtras(b);
            intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, idx);
            PostDetailActivity.this.startActivity(intent);
        });
    }
}
