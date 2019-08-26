package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.NewsSearchActivity;
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.news.marking.MarkingActivity;
import com.ciyuanplus.mobile.module.news.select_collect_or_report.SelectCollectOrReportActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.module.wiki.wiki_position.WikiPositionActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.video.SampleCoverVideo;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.MarkView;
import com.ciyuanplus.mobile.widget.RoundImageView;
import com.ciyuanplus.mobile.widget.ThreeGrideView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 个人中心  我的发布  和  他的个人中心 里面的list adapter
 */

public class MineNewsAdapter extends BaseAdapter {
    private final Activity mContext;

    private RequestOptions ops = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
            .dontAnimate().centerCrop();

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            int id = view.getId();

            if (id == R.id.iv_home_list_share
                    || id == R.id.m_list_item_mine_forum_post_share_image
                    || view.getId() == R.id.m_list_item_main_forum_daily_share_image) {
                FreshNewItem item = (FreshNewItem) view.getTag();
                Intent intent = new Intent(mContext, ShareNewsPopupActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, item);
                mContext.startActivity(intent);
            } else if (id == R.id.tv_like_count
                    || id == R.id.m_list_item_mine_forum_post_like_image
                    || view.getId() == R.id.m_list_item_main_forum_daily_like_image
                    || id == R.id.m_list_item_mine_forum_food_like_image
            ) {
                FreshNewItem item = (FreshNewItem) view.getTag();
                if (mContext instanceof OthersActivity) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_OTHERS,
                            StatisticsConstant.OP_OTHERS_NEWS_LIST_ITEM_LIKE_CLICK, item.postUuid);
                } else {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE,
                            StatisticsConstant.OP_MINE_NEWS_LIST_ITEM_LIKE_CLICK, item.postUuid);
                }
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                if (item.isLike == 1) cancelLikePost(item);
                else likePost(item);
//            }
            }
//            else if (id == R.id.m_list_item_mine_forum_food_like_image) {
//                FreshNewItem item = (FreshNewItem) view.getTag();
//                if (!LoginStateManager.isLogin()) {
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    mContext.startActivity(intent);
//                    return;
//                }
//                if (item.isDislike == 1) cancelCollectPost(item);
//                else collectPost(item);
//            }
            else if (view.getId() == R.id.m_list_item_main_forum_daily_head) {
                FreshNewItem item = (FreshNewItem) view.getTag(R.id.glide_item_tag);
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (item.isAnonymous == 1 || Utils.isStringEquals(item.someOne, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(mContext, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.someOne);
                mContext.startActivity(intent);
            } else if (view.getId() == R.id.m_list_item_mine_forum_news_more_image
                    || view.getId() == R.id.m_list_item_mine_forum_post_more_image
                    || view.getId() == R.id.m_list_item_main_forum_daily_more_image) {// 弹出删除和收藏的弹框
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                FreshNewItem item = (FreshNewItem) view.getTag();
                Intent intent = new Intent(mContext, SelectCollectOrReportActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, item);
                mContext.startActivity(intent);
            } else if (view.getId() == R.id.m_list_item_mine_forum_food_opera_text) {// 调到点评页面
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                FreshNewItem item = (FreshNewItem) view.getTag();
                if (item.isRated == 1) return;

                Intent intent = new Intent(mContext, MarkingActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, item);
                mContext.startActivity(intent);
            } else if (view.getId() == R.id.m_list_item_mine_forum_food_location_img
                    || view.getId() == R.id.m_list_item_mine_forum_food_location) {
                // 需要传WikiItem 到那个页面。 必传  四个参数， name、address、longitude、latitude
                FreshNewItem item = (FreshNewItem) view.getTag();
                WikiItem wikiItem = new WikiItem();
                wikiItem.name = item.placeName;
                wikiItem.address = item.placeAddress;
                wikiItem.longitude = item.longitude;
                wikiItem.latitude = item.latitude;
                Intent intent = new Intent(mContext, WikiPositionActivity.class);
                intent.putExtra(Constants.INTENT_POSITION_ITEM, wikiItem);
                mContext.startActivity(intent);
            }
        }
    };
    private final ArrayList<FreshNewItem> mNewsItems;
    private final int[] mForumTypes = {
            R.layout.item_home_list,
            R.layout.list_item_mine_forum_news,
            R.layout.list_item_mine_forum_post,
            R.layout.list_item_main_forum_daily,
            R.layout.list_item_mine_forum_food,
            R.layout.list_item_mine_forum_help};


    public MineNewsAdapter(Activity mContext, ArrayList<FreshNewItem> mNewsItems) { // 其他人发布的帖子
        this.mContext = mContext;
        this.mNewsItems = mNewsItems;
    }

    @Override
    public int getViewTypeCount() {
        return mForumTypes.length;
    }

    @Override
    public int getItemViewType(int position) {
        FreshNewItem item = mNewsItems.get(position);

//        if (item.bizType == FreshNewItem.FRESH_ITEM_ACTIVITY) return 1;// Banner
//        if (item.bizType == FreshNewItem.FRESH_ITEM_POST)
//            return item.renderType == 1 ? 0 : 1;// 说说和长文
//        if (item.bizType == FreshNewItem.FRESH_ITEM_DAILY) return 2;// 日签
//        if (item.bizType == FreshNewItem.FRESH_ITEM_NEWS) return 1;// 新闻搬运工
//        if (item.bizType == FreshNewItem.FRESH_ITEM_LIVE) return 3;//品鉴生活
//        if (item.bizType == FreshNewItem.FRESH_ITEM_ANSWER) return 4;//问答
//        if (item.bizType == FreshNewItem.FRESH_ITEM_NOTE || item.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION)
//            return 1;// 生活随手记
//        if (item.bizType == FreshNewItem.FRESH_ITEM_FOOD || item.bizType == FreshNewItem.FRESH_ITEM_COMMENT)
//            return 3;// 食品品鉴
//        return -1;
        return 0;
    }

    @Override
    public int getCount() {
        return mNewsItems == null ? 0 : mNewsItems.size();
    }

    @Override
    public FreshNewItem getItem(int i) {
        return mNewsItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        FreshNewItem item = mNewsItems.get(i);
        int type = getItemViewType(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(mForumTypes[type], null);
            switch (type) {
                case 0:
                    holder = new ViewHolderNews(convertView);
                    break;
                case 1:
                    holder = new ViewHolderPost(convertView);
                    break;
                case 2:
                    holder = new ViewHolderDaily(convertView);
                    break;
                case 3:
                    holder = new ViewHolderFood(convertView);
                    break;
                case 4:
                    holder = new ViewHolderHelp(convertView);
                    break;
            }
            convertView.setTag(holder);
        } else {
            switch (type) {
                case 0:
                    holder = (ViewHolderNews) convertView.getTag();
                    break;
                case 1:
                    holder = (ViewHolderPost) convertView.getTag();
                    break;
                case 2:
                    holder = (ViewHolderDaily) convertView.getTag();
                    break;
                case 3:
                    holder = (ViewHolderFood) convertView.getTag();
                    break;
                case 4:
                    holder = (ViewHolderHelp) convertView.getTag();
                    break;
            }
        }


        switch (type) {
            case 0:
                ViewHolderNews holderNews = (ViewHolderNews) holder;

                holderNews.mCommentCount.setText(Utils.formateNumber(item.commentCount));
                holderNews.like.setText(Utils.formateNumber(item.likeCount));

                Drawable drawable = mContext.getResources().getDrawable(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holderNews.like.setCompoundDrawables(drawable, null, null, null);

                holderNews.like.setTag(item);
                holderNews.like.setOnClickListener(myOnClickListener);
                holderNews.share.setTag(item);
                holderNews.share.setOnClickListener(myOnClickListener);
                holderNews.mBrowseCount.setText(String.valueOf(item.browseCount));

                if (!Utils.isStringEmpty(item.description)) {
                    holderNews.content.setVisibility(View.VISIBLE);
                    holderNews.content.setText(item.description.replace("\r", "\n"));
                } else {
                    holderNews.content.setVisibility(View.GONE);
                }

                holderNews.nickname.setText(item.nickname);

                if (!TextUtils.isEmpty(item.imgs)) {

                    if (!StringUtils.isEmpty(item.postType) && StringUtils.equals(item.postType, "1")) {
                        //视频帖子

                        holderNews.mJzvdStd.setVisibility(View.VISIBLE);
                        holderNews.images.setVisibility(View.GONE);

                        holderNews.mJzvdStd.loadCoverImage(Constants.IMAGE_LOAD_HEADER + item.imgs, R.mipmap.imgfail);
                        holderNews.mJzvdStd.setUpLazy(Constants.IMAGE_LOAD_HEADER + item.imgs, true, null, null, "这是title");
                        //增加title
                        holderNews.mJzvdStd.getTitleTextView().setVisibility(View.GONE);
                        //设置返回键
                        holderNews.mJzvdStd.getBackButton().setVisibility(View.GONE);
                        //设置全屏按键功能
                        holderNews.mJzvdStd.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holderNews.mJzvdStd.startWindowFullscreen(mContext, false, true);
                            }
                        });
                        //防止错位设置
                        holderNews.mJzvdStd.setPlayTag(this.getClass().getSimpleName());
                        holderNews.mJzvdStd.setPlayPosition(i);
                        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
                        holderNews.mJzvdStd.setAutoFullWithSize(true);
                        //音频焦点冲突时是否释放
                        holderNews.mJzvdStd.setReleaseWhenLossAudio(false);
                        //全屏动画
                        holderNews.mJzvdStd.setShowFullAnimation(true);
                        //小屏时不触摸滑动
                        holderNews.mJzvdStd.setIsTouchWiget(false);
                        //全屏是否需要lock功能

                    } else {

                        holderNews.mJzvdStd.setVisibility(View.GONE);
                        holderNews.images.setVisibility(View.VISIBLE);


                        ArrayList<ImageInfo> imageInfo = new ArrayList<>();

                        String[] paths = item.imgs.split(",");
                        for (String path : paths) {

                            ImageInfo info = new ImageInfo();
                            info.setThumbnailUrl(Constants.IMAGE_LOAD_HEADER + path);
                            info.setBigImageUrl(Constants.IMAGE_LOAD_HEADER + path);
                            imageInfo.add(info);

                        }
                        holderNews.images.setAdapter(new NineGridViewClickAdapterImp(mContext, imageInfo, item));
                    }
                }

                Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(ops).into(holderNews.headImage);

                holderNews.headImage.setTag(R.id.glide_item_tag, item);
                holderNews.headImage.setOnClickListener(myOnClickListener);
                break;
            case 1:
                ViewHolderPost holderPost = (ViewHolderPost) holder;
                holderPost.date.setText(Utils.getFormattedTimeString(item.createTime));// 时间

//                holderPost.location.setText(item.currentCommunityName);
                holderPost.comment.setText(Utils.formateNumber(item.commentCount));
                holderPost.like.setText(Utils.formateNumber(item.likeCount));
                holderPost.likeImage.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

                if (Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderPost.more.setClickable(false);
                } else {
                    holderPost.more.setTag(item);
                    holderPost.more.setOnClickListener(myOnClickListener);
                }
                holderPost.likeImage.setTag(item);
                holderPost.likeImage.setOnClickListener(myOnClickListener);
                holderPost.share.setTag(item);
                holderPost.share.setOnClickListener(myOnClickListener);


                String content = Utils.isStringEmpty(item.title) ? item.description : item.title;
                if (!Utils.isStringEmpty(content)) {
                    holderPost.postTitle.setVisibility(View.VISIBLE);
                    holderPost.postTitle.setText(content.replace("\r", "\n"));
                } else {
                    holderPost.postTitle.setVisibility(View.GONE);
                }
                if (Utils.isStringEmpty(item.imgs)) {
                    holderPost.postImage.setVisibility(View.GONE);
                } else {
                    holderPost.postImage.setVisibility(View.VISIBLE);
                    holderPost.postImage.setDataSource(item.imgs.split(","), item);
                }
                break;
            case 2:
                ViewHolderDaily holderDaily = (ViewHolderDaily) holder;


                holderDaily.time.setText(Utils.getFormattedTimeString(item.createTime));
                String[] result = Utils.formateDate(Long.parseLong(item.createTime));
                holderDaily.day.setText(result[2]);
                holderDaily.month.setText(result[0] + "/" + result[1]);
                holderDaily.week.setText(result[3]);
                Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.someThree).apply(ops).into(holderDaily.head);
                holderDaily.user.setText("摄影: " + item.someTwo);
                holderDaily.content.setText(item.contentText);
                if (Utils.isStringEmpty(item.imgs)) {
                    holderDaily.image.setVisibility(View.GONE);
                } else {
                    String[] images = item.imgs.split(",");
                    Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + images[0]).apply(ops).into(holderDaily.image);
                }

                holderDaily.likeNumber.setText(Utils.formateNumber(item.likeCount));
                holderDaily.commentNumber.setText(Utils.formateNumber(item.commentCount));
                holderDaily.like.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

                holderDaily.more.setTag(item);
                holderDaily.more.setOnClickListener(myOnClickListener);
                holderDaily.head.setTag(R.id.glide_item_tag, item);
                holderDaily.head.setOnClickListener(myOnClickListener);
                holderDaily.like.setTag(item);
                holderDaily.like.setOnClickListener(myOnClickListener);
                holderDaily.share.setTag(item);
                holderDaily.share.setOnClickListener(myOnClickListener);
                break;

            case 3:
                ViewHolderFood holderFood = (ViewHolderFood) holder;
                if (Utils.isStringEmpty(item.imgs)) {
                    holderFood.images.setVisibility(View.GONE);
                    holderFood.images.setTag("");
                } else {
                    holderFood.images.setVisibility(View.VISIBLE);
                    String oldImages = (String) holderFood.images.getTag();
                    if (!Utils.isStringEquals(oldImages, item.imgs)) { // 防止闪烁
                        holderFood.images.setTag(item.imgs);
                        holderFood.images.setDataSource(item.imgs.split(","), item);
                    }
                }
                holderFood.location.setText(item.placeName);

                if (!Utils.isStringEmpty(item.description)) {
                    holderFood.content.setVisibility(View.VISIBLE);
                    if (mContext instanceof NewsSearchActivity) {
                        holderFood.content.setText(Utils.matcherSearchTitle(0xff2279AB, item.description.replace("\r", "\n"), ((NewsSearchActivity) mContext).mSearchValue));
                    } else {
                        holderFood.content.setText(item.description.replace("\r", "\n"));
                    }
                } else {
                    holderFood.content.setVisibility(View.GONE);
                }

                if (mContext instanceof OthersActivity) {
                    holderFood.topMarkView.setVisibility(View.VISIBLE);
                    holderFood.topMarkView.setDisable(true);
                    holderFood.topMarkView.setValue(item.postScore);
                    holderFood.opera.setVisibility(View.VISIBLE);
                    holderFood.opera.setTag(item);
                    holderFood.opera.setOnClickListener(myOnClickListener);
                    holderFood.markView.setVisibility(View.GONE);
//                    holderFood.likeAlert.setText("收藏");
//                    if (item.isRated == 0) {
//                        holderFood.opera.setText("点评");
//                        holderFood.opera.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.img_star_halffull), null, null, null);
//                    } else {
//                        holderFood.opera.setText("已点评");
//                        holderFood.opera.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.img_star_grey), null, null, null);
//                    }
                } else {
                    holderFood.topMarkView.setVisibility(View.GONE);
                    holderFood.opera.setVisibility(View.GONE);
                    holderFood.markView.setVisibility(View.VISIBLE);
                    holderFood.markView.setDisable(true);
                    holderFood.markView.setValue(item.postScore);
//                    holderFood.likeAlert.setText("收藏");

                }

                holderFood.likeText.setText(Utils.formateNumber(item.likeCount));
                holderFood.commentText.setText(Utils.formateNumber(item.commentCount));
                holderFood.likeImage.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

                holderFood.likeImage.setTag(item);
                holderFood.likeImage.setOnClickListener(myOnClickListener);

                holderFood.shareImage.setTag(item);
                holderFood.shareImage.setOnClickListener(myOnClickListener);
                holderFood.location.setTag(item);
                holderFood.locationImg.setTag(item);
                holderFood.location.setOnClickListener(myOnClickListener);
                holderFood.locationImg.setOnClickListener(myOnClickListener);
                break;

            case 4:
                ViewHolderHelp holderHelp = (ViewHolderHelp) holder;
                holderHelp.date.setText(Utils.getFormattedTimeString(item.createTime));// 时间

//                holderHelp.location.setText(item.currentCommunityName);
                holderHelp.comment.setText(Utils.formateNumber(item.commentCount));
                holderHelp.like.setText(Utils.formateNumber(item.likeCount));
                holderHelp.likeImage.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

                if (Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderHelp.more.setClickable(false);
                } else {
                    holderHelp.more.setTag(item);
                    holderHelp.more.setOnClickListener(myOnClickListener);
                }
                holderHelp.likeImage.setTag(item);
                holderHelp.likeImage.setOnClickListener(myOnClickListener);
                holderHelp.share.setTag(item);
                holderHelp.share.setOnClickListener(myOnClickListener);


                String content2 = Utils.isStringEmpty(item.description) ? item.description : item.description;
                if (!Utils.isStringEmpty(content2)) {
                    holderHelp.postTitle.setVisibility(View.VISIBLE);
                    holderHelp.postTitle.setText(content2.replace("\r", "\n"));
                } else {
                    holderHelp.postTitle.setVisibility(View.GONE);
                }
                if (Utils.isStringEmpty(item.imgs)) {
                    holderHelp.postImage.setVisibility(View.GONE);
                } else {
                    holderHelp.postImage.setVisibility(View.VISIBLE);
                    holderHelp.postImage.setDataSource(item.imgs.split(","), item);
                }
                break;
        }
        return convertView;
    }

    // 收藏新鲜事
    private void cancelCollectPost(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_COLLECT);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ItemOperaApiParameter(item.postUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("已取消收藏").show();

                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, item.postUuid));
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance("mmmmmm 操作失败").show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 取消收藏新鲜事
    private void collectPost(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_COLLECT);
        postRequest.setHttpBody(new ItemOperaApiParameter(item.postUuid).getRequestBody());
        postRequest.setMethod(HttpMethods.Post);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("已收藏").show();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, item.postUuid));

                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance("操作失败").show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    // 赞新鲜事
    private void likePost(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_zan);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new LikeOperaApiParameter(item.bizType + "", item.postUuid).getRequestBody());
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
                    item.isLike = 1;
                    item.likeCount++;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, item));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance("操作失败").show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 取消赞新鲜事
    private void cancelLikePost(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_LIKE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ItemOperaApiParameter(item.postUuid).getRequestBody());
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
                    item.isLike = 0;
                    if (item.likeCount > 0) item.likeCount--;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, item));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance("操作失败").show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    static class ViewHolder {


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolderNews extends ViewHolder {

        @BindView(R.id.tv_content)
        TextView content;
        @BindView(R.id.nineGrid)
        NineGridView images;
        @BindView(R.id.tv_like_count)
        TextView like;
        @BindView(R.id.iv_home_list_share)
        ImageView share;
        @BindView(R.id.riv_head_image)
        RoundImageView headImage;
        @BindView(R.id.tv_name)
        public TextView nickname;
        @BindView(R.id.tv_browse_count)
        TextView mBrowseCount;
        @BindView(R.id.tv_comment_count)
        TextView mCommentCount;
        @BindView(R.id.video_item_player)
        SampleCoverVideo mJzvdStd;

        ViewHolderNews(View view) {
            super(view);
        }
    }

    class ViewHolderPost extends ViewHolder {
        @BindView(R.id.m_list_item_mine_forum_post_time_text)
        TextView date;
        @BindView(R.id.m_list_item_mine_forum_post_zone_text)
        TextView location;
        @BindView(R.id.m_list_item_mine_forum_post_comment_text)
        TextView comment;
        @BindView(R.id.m_list_item_mine_forum_post_like_text)
        TextView like;
        @BindView(R.id.m_list_item_mine_forum_post_like_image)
        ImageView likeImage;
        @BindView(R.id.m_list_item_mine_forum_post_post_title)
        TextView postTitle;
        @BindView(R.id.m_list_item_mine_forum_post_post_image)
        ThreeGrideView postImage;
        @BindView(R.id.m_list_item_mine_forum_post_more_image)
        ImageView more;
        @BindView(R.id.m_list_item_mine_forum_post_share_image)
        ImageView share;

        ViewHolderPost(View view) {
            super(view);
        }
    }

    class ViewHolderDaily extends ViewHolder {
        @BindView(R.id.m_list_item_main_forum_daily_like_image)
        ImageView like;
        @BindView(R.id.m_list_item_main_forum_daily_like_text)
        TextView likeNumber;
        @BindView(R.id.m_list_item_main_forum_daily_comment_text)
        TextView commentNumber;
        @BindView(R.id.m_list_item_main_forum_daily_share_image)
        ImageView share;
        @BindView(R.id.m_list_item_main_forum_daily_day)
        TextView day;
        @BindView(R.id.m_list_item_main_forum_daily_time)
        TextView time;
        @BindView(R.id.m_list_item_main_forum_daily_week)
        TextView week;
        @BindView(R.id.m_list_item_main_forum_daily_month)
        TextView month;
        @BindView(R.id.m_list_item_main_forum_daily_head)
        ImageView head;
        @BindView(R.id.m_list_item_main_forum_daily_image)
        ImageView image;
        @BindView(R.id.m_list_item_main_forum_daily_user_name)
        TextView user;
        @BindView(R.id.m_list_item_main_forum_daily_content)
        TextView content;
        @BindView(R.id.m_list_item_main_forum_daily_more_image)
        ImageView more;

        ViewHolderDaily(View view) {
            super(view);
        }
    }

    class ViewHolderFood extends ViewHolder {
        @BindView(R.id.m_list_item_mine_forum_food_top_mark_view)
        MarkView topMarkView;
        @BindView(R.id.m_list_item_mine_forum_food_images)
        ThreeGrideView images;
        @BindView(R.id.m_list_item_mine_forum_food_location)
        TextView location;
        @BindView(R.id.m_list_item_mine_forum_food_location_img)
        ImageView locationImg;
        @BindView(R.id.m_list_item_mine_forum_food_text)
        TextView content;
        @BindView(R.id.m_list_item_mine_forum_food_mark_view)
        MarkView markView;
        @BindView(R.id.m_list_item_mine_forum_food_like_image)
        ImageView likeImage;
        @BindView(R.id.m_list_item_mine_forum_food_like_text)
        TextView likeText;
        @BindView(R.id.m_list_item_mine_forum_food_comment_image)
        ImageView commentImage;
        @BindView(R.id.m_list_item_mine_forum_food_comment_text)
        TextView commentText;
        @BindView(R.id.m_list_item_mine_forum_food_share_image)
        ImageView shareImage;
        @BindView(R.id.m_list_item_mine_forum_food_opera_text)
        TextView opera;

        ViewHolderFood(View view) {
            super(view);
        }
    }

    class ViewHolderHelp extends ViewHolder {
        @BindView(R.id.m_list_item_mine_forum_post_time_text)
        TextView date;
        @BindView(R.id.m_list_item_mine_forum_post_zone_text)
        TextView location;
        @BindView(R.id.m_list_item_mine_forum_post_comment_text)
        TextView comment;
        @BindView(R.id.m_list_item_mine_forum_post_like_text)
        TextView like;
        @BindView(R.id.m_list_item_mine_forum_post_like_image)
        ImageView likeImage;
        @BindView(R.id.m_list_item_mine_forum_post_post_title)
        TextView postTitle;
        @BindView(R.id.m_list_item_mine_forum_post_post_image)
        ThreeGrideView postImage;
        @BindView(R.id.m_list_item_mine_forum_post_more_image)
        ImageView more;
        @BindView(R.id.m_list_item_mine_forum_post_share_image)
        ImageView share;

        ViewHolderHelp(View view) {
            super(view);
        }
    }

}
