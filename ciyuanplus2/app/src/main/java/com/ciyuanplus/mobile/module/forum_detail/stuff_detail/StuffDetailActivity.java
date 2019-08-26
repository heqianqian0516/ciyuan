package com.ciyuanplus.mobile.module.forum_detail.stuff_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.activity.news.ReportPostActivity;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.module.popup.change_stuff_state.ChangeStuffStateActivity;
import com.ciyuanplus.mobile.module.popup.select_edit_or_delete_stuff.SelectEditOrDeleteStuffActivity;
import com.ciyuanplus.mobile.module.start_forum.start_stuff.StartStuffActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.ChangeStuffStatusApiParameter;
import com.ciyuanplus.mobile.net.parameter.DeleteMyNewsApiParameter;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.sendtion.xrichtext.RichTextView;
import com.sendtion.xrichtext.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 买卖详情
 * Created by Alen on 2017/5/12.
 */

public class StuffDetailActivity extends ForumDetailActivity implements EventCenterManager.OnHandleEventListener {

    private ImageView mHeadIconImage;
    private ImageView mSexIconImage;
    private ImageView mNeighborImage;
    private TextView mNameText;
    private TextView mTimeText;
    private TextView mTitleText;
    private ImageView mFollowImage;
    private TextView mCoummunityName;
    private TextView mPriceView;
    private TextView mStateView;
    private TextView mStuffDistance;
    private RichTextView mContentView;


    private final ArrayList<String> mImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatisticsManager.onEventInfo("detail_stuff", "detail_stuff_page_load");

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
        mSexIconImage.setVisibility(View.VISIBLE);
        mNameText.setText(mPresenter.mItem.nickname);
        if (Utils.isStringEquals(mPresenter.mItem.currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid) && !Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mNeighborImage.setVisibility(View.VISIBLE);
        } else {
            mNeighborImage.setVisibility(View.INVISIBLE);
        }
        if (!Utils.isStringEmpty(mPresenter.mItem.photo)) {
            if (!Utils.isStringEquals(mPresenter.mItem.photo, (String) mHeadIconImage.getTag(R.id.glide_item_tag)))// 防止闪动
                Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mItem.photo)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                        .dontAnimate().centerCrop()).into(mHeadIconImage);
//            ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + mItem.photo, mHeadIconImage, new ImageSize(75, 75));
            mHeadIconImage.setTag(R.id.glide_item_tag, mPresenter.mItem.photo);

//            mCoummunityName.setVisibility(View.VISIBLE);

        }
//        mCoummunityName.setText(mPresenter.mItem.currentCommunityName);
        mSexIconImage.setImageResource(mPresenter.mItem.getSexImageResource());

        mTimeText.setText(Utils.getFormattedTimeString(mPresenter.mItem.createTime));
        mTitleText.setText(mPresenter.mItem.title.replace("\r", "\n"));
        showContentData(mPresenter.mItem.contentText);
        mPriceView.setText(mPresenter.mItem.price == 0.0f ? "免费" : "¥" + mPresenter.mItem.price + "");
        mStateView.setText(mPresenter.mItem.getStatus());
        mStuffDistance.setText(Utils.formateDistance(mPresenter.mItem.distance));

        if (mPresenter.mItem.isFollow != 1 && !Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mFollowImage.setVisibility(View.VISIBLE);
            mFollowImage.setOnClickListener(myOnClickListener);
        } else {
            mFollowImage.setVisibility(View.GONE);
        }
//        if (Utils.isStringEquals(UserInfoData.getInstance().getUserInfoItem().uuid, mPresenter.mItem.userUuid)) {
//            mCommonDetailContactUser.setVisibility(View.GONE);
//        } else {
//            mCommonDetailContactUser.setVisibility(View.VISIBLE);
//        }
//        if(!Utils.isStringEmpty(mPresenter.mItem.imgs)){
//            if(!Utils.isStringEquals(mPresenter.mItem.imgs, (String) mNineGrid.getTag())){// 防止闪动
//                mNineGrid.setTag(mPresenter.mItem.imgs);
//                mNineGrid.setDataSource(mPresenter.mItem.imgs.split(","));
//            }
//        }
//        else mNineGrid.setVisibility(View.GONE);
    }

    @Override
    public void bindTopLayout() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeadView = inflater != null ? inflater.inflate(R.layout.layout_stuff_detail_top, null) : null;
        mCommentList.addHeaderView(mHeadView);

        mNullCommentLayout = mHeadView.findViewById( R.id.m_stuff_detail_null_lp);
        mHeadIconImage = mHeadView.findViewById( R.id.m_stuff_detail_head_image);
        mNameText = mHeadView.findViewById( R.id.m_stuff_detail_name_text);
        mNeighborImage = mHeadView.findViewById( R.id.m_stuff_detail_neighbor_image);
        mTimeText = mHeadView.findViewById( R.id.m_stuff_detail_time_text);
        mSexIconImage = mHeadView.findViewById( R.id.m_stuff_detail_sex_image);
        mTitleText = mHeadView.findViewById( R.id.m_stuff_detail_title_text);
        mContentView = mHeadView.findViewById( R.id.m_stuff_detail_content);
        mFollowImage = mHeadView.findViewById( R.id.m_stuff_detail_follow_text);
        mCoummunityName = mHeadView.findViewById( R.id.m_stuff_detail_community_name);
        mPriceView = mHeadView.findViewById( R.id.m_stuff_detail_price);
        mStateView = mHeadView.findViewById( R.id.m_stuff_detail_state);
        mStuffDistance = mHeadView.findViewById( R.id.m_stuff_detail_stuff_distance);
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
                StuffDetailActivity.this.startActivity(intent);
            }
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this);
//        mHeadIconImage.requestFocus();//解决进入页面会自动滑动的问题
    }

    // 打开 编辑的选择activity
    @Override
    public void showNewsOperaActivity(int type) {
        if (mPresenter.mItem == null) return;
        Intent intent = new Intent(this, SelectEditOrDeleteStuffActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, type);
        intent.putExtra(Constants.INTENT_POST_HAS_COLLECTED, mPresenter.mItem.isDislike);
        intent.putExtra(Constants.INTENT_POST_STATUS, mPresenter.mItem.status);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_EDIT_DELETE_NEWS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_EDIT_DELETE_NEWS && resultCode == RESULT_OK) {
            String opera = data.getStringExtra(SelectEditOrDeleteStuffActivity.SELECTED);
            if (Utils.isStringEquals(SelectEditOrDeleteStuffActivity.EDIT, opera)) {
                Intent intent = new Intent(StuffDetailActivity.this, StartStuffActivity.class);
                intent.putExtra(Constants.INTENT_UDPATE_NENWS_ITEM, mPresenter.mItem);
                startActivity(intent);
            } else if (Utils.isStringEquals(SelectEditOrDeleteStuffActivity.DELETE, opera)) {
                deleteStuff(mPresenter.mItem);
            } else if (Utils.isStringEquals(SelectEditOrDeleteStuffActivity.COLLECT, opera)) {
                if (mPresenter.mItem.isDislike == 1) mPresenter.cancelCollectPost();
                else mPresenter.collectPost();
            } else if (Utils.isStringEquals(SelectEditOrDeleteStuffActivity.REPORT, opera)) {
                Intent intent = new Intent(this, ReportPostActivity.class);
                intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, 2);
                intent.putExtra(Constants.INTENT_POST_ID, mPresenter.mItem.postUuid);
                startActivity(intent);
            } else if (Utils.isStringEquals(SelectEditOrDeleteStuffActivity.CHANGE, opera)) {// 修改物品状态
                Intent intent = new Intent(this, ChangeStuffStateActivity.class);
                this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_STUFF_STATE);
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_DELETE_COMMENT && resultCode == RESULT_OK) {
            String opera = data.getStringExtra(SelectDeleteCommentActivity.SELECTED);
            if (Utils.isStringEquals(SelectDeleteCommentActivity.DELETE_COMENT, opera)) {
                mPresenter.deleteComment(mPresenter.mTryDeleteComment);
            } else if (Utils.isStringEquals(SelectDeleteCommentActivity.DELETE_REPLY, opera)) {
                mPresenter.deleteReply(mPresenter.mTryDeleteReply);
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_STUFF_STATE && resultCode == RESULT_OK) {
            String opera = data.getStringExtra(ChangeStuffStateActivity.SELECTED);
            if (Utils.isStringEquals(ChangeStuffStateActivity.SELLED, opera)) {// 已卖出
                changeStuffState(8);
            } else if (Utils.isStringEquals(ChangeStuffStateActivity.SELLING, opera)) {//
                changeStuffState(6);
            } else if (Utils.isStringEquals(ChangeStuffStateActivity.ORDERED, opera)) {//
                changeStuffState(7);
            }
        }
    }

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            int id = view.getId();
            if (id == R.id.m_stuff_detail_follow_text) {
//            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_stuff_detail, StatisticsConstant.OP_stuff_detail_FOLLOW_CLICK);
                if (mPresenter.mItem == null) return;
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(StuffDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                mPresenter.requestFollowUser();
            } else if (id == R.id.m_stuff_detail_head_image) {
//            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_stuff_detail, StatisticsConstant.OP_stuff_detail_USER_HEAD_CLICK);
                if (mPresenter.mItem == null) return;
                // 是自己发布的帖子，不允许进入他人页面
                if (Utils.isStringEquals(mPresenter.mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(StuffDetailActivity.this, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, mPresenter.mItem.userUuid);
                startActivity(intent);
            }
        }
    };

    private void changeStuffState(final int status) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CHANGE_STUFF_STATUS);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ChangeStuffStatusApiParameter(mPresenter.mItem.postUuid, status + "").getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mPresenter.mItem.status = status;
                    updateView();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
                        Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void deleteStuff(FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_DELETE_MY_PUBLISH_POST_URL);
        postRequest.setHttpBody(new DeleteMyNewsApiParameter(item.postUuid, item.communityUuid).getRequestBody());
        postRequest.setMethod(HttpMethods.Post);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("删除成功").show();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, mPresenter.mItem.postUuid));
                    finish();
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

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
