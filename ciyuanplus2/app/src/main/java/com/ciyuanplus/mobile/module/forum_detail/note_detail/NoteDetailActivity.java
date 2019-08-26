package com.ciyuanplus.mobile.module.forum_detail.note_detail;

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
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.activity.news.ReportPostActivity;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.activity.news.SelectEditOrDeleteNewsActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.module.start_forum.start_news.StartNewsActivity;
import com.ciyuanplus.mobile.module.start_forum.start_note.StartNoteActivity;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.sendtion.xrichtext.RichTextView;
import com.sendtion.xrichtext.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/5/12.
 */

public class NoteDetailActivity extends ForumDetailActivity implements EventCenterManager.OnHandleEventListener {

    private ImageView mHeadIconImage;
    private ImageView mSexIconImage;
    private ImageView mNeighborImage;
    private TextView mNameText;
    private TextView mTimeText;
    private TextView mTitleText;
    private ImageView mFollowImage;
    private TextView mCoummunityName;
    private RichTextView mContentView;

    private final ArrayList<String> mImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void bindTopLayout() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeadView = inflater != null ? inflater.inflate(R.layout.activity_note_detail_layout, null) : null;
        mCommentList.addHeaderView(mHeadView);

        mNullCommentLayout = mHeadView.findViewById(R.id.m_note_detail_null_lp);

        mHeadIconImage = mHeadView.findViewById(R.id.m_note_detail_head_image);
        mNameText = mHeadView.findViewById(R.id.m_note_detail_name_text);
        mNeighborImage = mHeadView.findViewById(R.id.m_note_detail_neighbor_image);
        mTimeText = mHeadView.findViewById(R.id.m_note_detail_time_text);
        mSexIconImage = mHeadView.findViewById(R.id.m_note_detail_sex_image);
        mTitleText = mHeadView.findViewById(R.id.m_note_detail_title_text);
        mContentView = mHeadView.findViewById(R.id.m_note_detail_content);
        mFollowImage = mHeadView.findViewById(R.id.m_note_detail_follow_text);
        mCoummunityName = mHeadView.findViewById(R.id.m_note_detail_community_name);
        mHeadIconImage.setOnClickListener(myOnClickListener);

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
                NoteDetailActivity.this.startActivity(intent);
            }
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);

//        mHeadIconImage.requestFocus();//解决进入页面会自动滑动的问题
    }

    // 更新界面信息
    @Override
    public void updateView() {
        if (mPresenter.mItem == null) return;
        super.updateView();
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
//            mCoummunityName.setVisibility(View.INVISIBLE);
        } else if (!Utils.isStringEmpty(mPresenter.mItem.photo)) {
            if (!Utils.isStringEquals(mPresenter.mItem.photo, (String) mHeadIconImage.getTag(R.id.glide_item_tag)))// 防止闪动
                Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                        .dontAnimate().centerCrop()).into(mHeadIconImage);
//            ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + mItem.photo, mHeadIconImage, new ImageSize(75, 75));
            mHeadIconImage.setTag(R.id.glide_item_tag, mPresenter.mItem.photo);

//            mCoummunityName.setVisibility(View.VISIBLE);

        }
//        mCoummunityName.setText(mPresenter.mItem.currentCommunityName);
        mSexIconImage.setImageResource(mPresenter.mItem.getSexImageResource());

        mTimeText.setText(Utils.getFormattedTimeString(mPresenter.mItem.createTime));
        if (!Utils.isStringEmpty(mPresenter.mItem.title)) {
            mTitleText.setText(mPresenter.mItem.title.replace("\r", "\n"));
            mTitleText.setVisibility(View.VISIBLE);
        } else mTitleText.setVisibility(View.GONE);
        showContentData(mPresenter.mItem.contentText);

        if (mPresenter.mItem.isAnonymous != 1 && mPresenter.mItem.isFollow != 1 && !Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mFollowImage.setVisibility(View.VISIBLE);
            mFollowImage.setOnClickListener(myOnClickListener);
        } else {
            mFollowImage.setVisibility(View.GONE);
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
                if (mPresenter.mItem.bizType == FreshNewItem.FRESH_ITEM_NOTE || mPresenter.mItem.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {
                    Intent intent = new Intent(NoteDetailActivity.this, StartNoteActivity.class);
                    intent.putExtra(Constants.INTENT_UDPATE_NENWS_ITEM, mPresenter.mItem);
                    startActivity(intent);
                } else if (mPresenter.mItem.bizType == FreshNewItem.FRESH_ITEM_NEWS) {
                    Intent intent = new Intent(NoteDetailActivity.this, StartNewsActivity.class);
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
            if (id == R.id.m_note_detail_follow_text) {
                if (mPresenter.mItem == null) return;
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(NoteDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                mPresenter.requestFollowUser();
            } else if (id == R.id.m_note_detail_head_image) {
                if (mPresenter.mItem == null) return;
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (mPresenter.mItem.isAnonymous == 1 || Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(NoteDetailActivity.this, OthersActivity.class);
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
}
