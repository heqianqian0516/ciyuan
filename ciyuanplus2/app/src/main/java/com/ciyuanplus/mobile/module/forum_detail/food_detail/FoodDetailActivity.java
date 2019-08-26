package com.ciyuanplus.mobile.module.forum_detail.food_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.ciyuanplus.mobile.module.forum_detail.rate_list.RateListActivity;
import com.ciyuanplus.mobile.module.forum_detail.want_list.WantListActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.module.start_forum.start_food.StartFoodActivity;
import com.ciyuanplus.mobile.module.wiki.wiki_position.WikiPositionActivity;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.DuplicateHeadIconImageView2;
import com.ciyuanplus.mobile.widget.MarkView;
import com.sendtion.xrichtext.RichTextView;
import com.sendtion.xrichtext.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/5/12.
 * 美食品鉴 推荐 详情页面
 */

public class FoodDetailActivity extends ForumDetailActivity implements EventCenterManager.OnHandleEventListener {

    private ImageView mHeadIconImage;
    private ImageView mSexIconImage;
    private ImageView mNeighborImage;
    private TextView mNameText;
    private TextView mTimeText;
    private ImageView mFollowImage;
    private TextView mCoummunityName;
    private RichTextView mContentView;
    private TextView mLocationiView;
    private MarkView mMarkView;
    private TextView mMoreDetailAlert2;
    private TextView mMoreDetailNumber2;
    private DuplicateHeadIconImageView2 mMoreDetailImage2;
    private ImageView mRateListInfoImage;
    private TextView mRateListInfoScore;
    private TextView mRateListPlaceName;
    private MarkView mRateListMarkView;
    private TextView mRateListNumbers;
    private RelativeLayout mWantListLayout;

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
        mHeadView = inflater != null ? inflater.inflate(R.layout.activity_food_detail_layout, null) : null;
        mCommentList.addHeaderView(mHeadView);

        mNullCommentLayout = mHeadView.findViewById(R.id.m_food_detail_null_lp);

        mHeadIconImage = mHeadView.findViewById( R.id.m_food_detail_head_image);
        mNameText = mHeadView.findViewById( R.id.m_food_detail_name_text);
        mNeighborImage = mHeadView.findViewById( R.id.m_food_detail_neighbor_image);
        mTimeText = mHeadView.findViewById( R.id.m_food_detail_time_text);
        mSexIconImage = mHeadView.findViewById( R.id.m_food_detail_sex_image);
        mLocationiView = mHeadView.findViewById( R.id.m_food_detail_location);
        mContentView = mHeadView.findViewById( R.id.m_food_detail_content);
        mFollowImage = mHeadView.findViewById( R.id.m_food_detail_follow_text);
        mCoummunityName = mHeadView.findViewById( R.id.m_food_detail_community_name);
        mMarkView = mHeadView.findViewById( R.id.m_food_detail_mark_view);

        RelativeLayout rateListInfoLayout = mHeadView.findViewById(R.id.m_food_detail_info_layout);
        mRateListInfoImage = mHeadView.findViewById( R.id.m_food_detail_info_image);
        mRateListInfoScore = mHeadView.findViewById( R.id.m_food_detail_info_score);
        mRateListPlaceName = mHeadView.findViewById( R.id.m_food_detail_place_name);
        TextView rateListAlert = mHeadView.findViewById(R.id.m_food_detail_alert);
        mRateListMarkView = mHeadView.findViewById( R.id.m_food_detail_position_mark_view);
        mRateListNumbers = mHeadView.findViewById( R.id.m_food_detail_numbers);

        mMoreDetailAlert2 = mHeadView.findViewById( R.id.m_food_detail_more_detail_lp_2_alert);
        mMoreDetailNumber2 = mHeadView.findViewById( R.id.m_food_detail_more_detail_lp_2_number);
        mMoreDetailImage2 = mHeadView.findViewById( R.id.m_food_detail_more_detail_lp_2_images);

        mWantListLayout = mHeadView.findViewById( R.id.m_food_detail_more_detail_lp_2);
        mWantListLayout.setOnClickListener(myOnClickListener);
        mHeadIconImage.setOnClickListener(myOnClickListener);
        rateListInfoLayout.setOnClickListener(myOnClickListener);
        mLocationiView.setOnClickListener(myOnClickListener);
        mRateListPlaceName.setOnClickListener(myOnClickListener);
        mHeadView.findViewById( R.id.m_food_detail_location_img).setOnClickListener(myOnClickListener);

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
                FoodDetailActivity.this.startActivity(intent);
            }
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);

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
                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                        .dontAnimate().centerCrop()).into(mHeadIconImage);
//            ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + mItem.photo, mHeadIconImage, new ImageSize(75, 75));
            mHeadIconImage.setTag(R.id.glide_item_tag, mPresenter.mItem.photo);

//            mCoummunityName.setVisibility(View.VISIBLE);

        }
        mCoummunityName.setText(mPresenter.mItem.currentCommunityName);
        mSexIconImage.setImageResource(mPresenter.mItem.getSexImageResource());
        mLocationiView.setText(mPresenter.mItem.placeName);
        mMarkView.setValue(mPresenter.mItem.postScore);

        updateWantList();

        mTimeText.setText(Utils.getFormattedTimeString(mPresenter.mItem.createTime));
        showContentData(mPresenter.mItem.contentText);

        if (mPresenter.mItem.isAnonymous != 1 && mPresenter.mItem.isFollow != 1 && !Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mFollowImage.setVisibility(View.VISIBLE);
            mFollowImage.setOnClickListener(myOnClickListener);
        } else {
            mFollowImage.setVisibility(View.GONE);
        }

        updateRateList();
    }


    private void updateWantList() {
        if (mPresenter.mItem.wishPhotos == null || mPresenter.mItem.wishPhotos.length == 0)
            mWantListLayout.setVisibility(View.GONE);
        else {
            mWantListLayout.setVisibility(View.VISIBLE);
            mMoreDetailAlert2.setText("收藏");//mPresenter.mItem.bizType == FreshNewItem.FRESH_ITEM_FOOD ? "想吃" : "想体验"
            mMoreDetailNumber2.setText((mPresenter.mItem.wishPhotos == null ? 0 : mPresenter.mItem.wishPhotos.length) + " >");
            mMoreDetailImage2.setDataSource(mPresenter.mItem.wishPhotos);
        }
    }

    private void updateRateList() {
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.imgs.split(",")[0])
                .into(mRateListInfoImage);
        mRateListInfoScore.setText(mPresenter.mItem.placeScore + "");
        mRateListPlaceName.setText(mPresenter.mItem.placeName);
        mRateListMarkView.setValue(mPresenter.mItem.placeScore);
        mRateListNumbers.setText((mPresenter.mItem.ratePhotos == null ? 0 : mPresenter.mItem.ratePhotos.length) + "人评过");
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
                Intent intent = new Intent(FoodDetailActivity.this, StartFoodActivity.class);
                intent.putExtra(Constants.INTENT_UDPATE_NENWS_ITEM, mPresenter.mItem);
                startActivity(intent);
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
            if (id == R.id.m_food_detail_follow_text) {
                if (mPresenter.mItem == null) return;
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(FoodDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                mPresenter.requestFollowUser();
            } else if (id == R.id.m_food_detail_head_image) {
                if (mPresenter.mItem == null) return;
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (mPresenter.mItem.isAnonymous == 1 || Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(FoodDetailActivity.this, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, mPresenter.mItem.userUuid);
                startActivity(intent);
            } else if (id == R.id.m_food_detail_info_layout) {
                if (mPresenter.mItem == null) return;
                Intent intent = new Intent(FoodDetailActivity.this, RateListActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, mPresenter.mItem);
                startActivity(intent);
            } else if (id == R.id.m_food_detail_more_detail_lp_2) {
                if (mPresenter.mItem == null) return;
                Intent intent = new Intent(FoodDetailActivity.this, WantListActivity.class);
                intent.putExtra(Constants.INTENT_POST_ID, mPresenter.mItem.postUuid);
                intent.putExtra(Constants.INTENT_PLACE_ID, mPresenter.mItem.placeId);
                intent.putExtra(Constants.INTENT_BIZE_TYPE, mPresenter.mItem.bizType);
                startActivity(intent);
            } else if (id == R.id.m_food_detail_location_img || id == R.id.m_food_detail_location
                    || id == R.id.m_food_detail_place_name) {
                // 需要传WikiItem 到那个页面。 必传  四个参数， name、address、longitude、latitude
                WikiItem wikiItem = new WikiItem();
                wikiItem.name = mPresenter.mItem.placeName;
                wikiItem.address = mPresenter.mItem.placeAddress;
                wikiItem.longitude = mPresenter.mItem.longitude;
                wikiItem.latitude = mPresenter.mItem.latitude;
                Intent intent = new Intent(FoodDetailActivity.this, WikiPositionActivity.class);
                intent.putExtra(Constants.INTENT_POSITION_ITEM, wikiItem);
                startActivity(intent);
            }
        }
    };

    private void showContentData(final String content) {
        String oldContent = (String) mContentView.getTag();
        if (Utils.isStringEquals(oldContent, content)) return;
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
            mContentView.setTag(content);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM) {
            mPresenter.requestForumDetail();
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            if (Utils.isStringEquals(postUuid, mPresenter.mItem.postUuid)) {
                mPresenter.mItem.isRated = 1;
                //updateBottomView();
                mPresenter.requestForumDetail();
                mPresenter.requestPostComments(true);
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            if (Utils.isStringEquals(postUuid, mPresenter.mItem.postUuid)) {
                mPresenter.mItem.isRated = 0;
                //updateBottomView();
                mPresenter.requestForumDetail();
                mPresenter.requestPostComments(true);
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            if (Utils.isStringEquals(postUuid, mPresenter.mItem.postUuid)) {
                mPresenter.mItem.isDislike = 1;
                mPresenter.mItem.dislikeCount++;
                mPresenter.requestForumDetail();
                //updateBottomView();
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            if (Utils.isStringEquals(postUuid, mPresenter.mItem.postUuid)) {
                mPresenter.mItem.isDislike = 0;
                mPresenter.mItem.dislikeCount--;
                mPresenter.requestForumDetail();
                //updateBottomView();
            }
        }
    }
}
