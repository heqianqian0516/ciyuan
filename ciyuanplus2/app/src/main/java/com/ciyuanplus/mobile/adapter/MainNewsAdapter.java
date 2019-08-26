package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.mine.MyCollectionActivity;
import com.ciyuanplus.mobile.activity.news.NewsSearchActivity;
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.module.wiki.wiki_position.WikiPositionActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter;
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter;
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.MarkView;
import com.ciyuanplus.mobile.widget.RoundImageView;
import com.ciyuanplus.mobile.widget.ThreeGrideView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 主页面新鲜事tab 里面的list adapter   我的喜欢、 世界 、 小区  帖子列表
 */

public class MainNewsAdapter extends RecyclerView.Adapter<MainNewsAdapter.ViewHolder> {
    private final Activity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if (view.getId() == R.id.iv_follow
                    || view.getId() == R.id.iv_follow
                    || view.getId() == R.id.iv_follow
                    || view.getId() == R.id.iv_follow) {
                FreshNewItem item = (FreshNewItem) view.getTag();
                if (mContext instanceof MyCollectionActivity) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_LIKE, StatisticsConstant.OP_MINE_LIKE_LIST_ITEM_FOLLOW_CLICK, item.postUuid);
                } else if (mContext instanceof NewsSearchActivity) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, StatisticsConstant.OP_SEARCH_NEWS_ITEM_FOLLOW_CLICK, item.postUuid);

                } else {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_NEWS, StatisticsConstant.OP_NEWS_ITEM_FOLLOW_CLICK, item.postUuid);
                }
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                requestFollowUser(item);
            } else if (view.getId() == R.id.m_list_item_main_forum_news_share_image
                    || view.getId() == R.id.m_list_item_main_forum_post_share_image
                    || view.getId() == R.id.m_list_item_main_forum_stuff_share_image
                    || view.getId() == R.id.m_list_item_main_forum_daily_share_image
                    || view.getId() == R.id.m_list_item_main_forum_food_share_image) {
                FreshNewItem item = (FreshNewItem) view.getTag();
                Intent intent = new Intent(mContext, ShareNewsPopupActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, item);
                mContext.startActivity(intent);
            } else if (view.getId() == R.id.m_list_item_main_forum_daily_head) {
                FreshNewItem item = (FreshNewItem) view.getTag(R.id.glide_item_tag);
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (item.isAnonymous == 1 || Utils.isStringEquals(item.someOne, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(mContext, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.someOne);
                mContext.startActivity(intent);
            } else if (view.getId() == R.id.m_list_item_main_forum_news_like_image
                    || view.getId() == R.id.m_list_item_main_forum_post_like_image
                    || view.getId() == R.id.m_list_item_main_forum_stuff_like_image
                    || view.getId() == R.id.m_list_item_main_forum_daily_like_image) {
                FreshNewItem item = (FreshNewItem) view.getTag();

                if (mContext instanceof MyCollectionActivity) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_LIKE, StatisticsConstant.OP_MINE_LIKE_LIST_ITEM_LIKE_CLICK, item.postUuid);
                } else if (mContext instanceof NewsSearchActivity) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, StatisticsConstant.OP_SEARCH_NEWS_ITEM_LIKE_CLICK, item.postUuid);

                } else {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_NEWS, StatisticsConstant.OP_NEWS_ITEM_LIKE_CLICK, item.postUuid);
                }
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                if (item.isLike == 1) cancelLikePost(item);
                else likePost(item);
            }

//            else if (view.getId() == R.id.m_list_item_main_forum_food_like_image) {
//                FreshNewItem item = (FreshNewItem) view.getTag();
//                if (!LoginStateManager.isLogin()) {
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    mContext.startActivity(intent);
//                    return;
//                }
//                if (item.isDislike == 1) cancelCollectPost(item);
//                else collectPost(item);
//            }

            else if (view.getId() == R.id.riv_head_image
                    || view.getId() == R.id.riv_head_image
                    || view.getId() == R.id.riv_head_image
                    || view.getId() == R.id.riv_head_image) {
                FreshNewItem item = (FreshNewItem) view.getTag(R.id.glide_item_tag);

                if (mContext instanceof MyCollectionActivity) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_LIKE, StatisticsConstant.OP_MINE_LIKE_LIST_ITEM_HEAD_CLICK, item.postUuid);
                } else if (mContext instanceof NewsSearchActivity) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, StatisticsConstant.OP_SEARCH_NEWS_ITEM_USER_HEAD_CLICK, item.postUuid);

                } else {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_NEWS, StatisticsConstant.OP_NEWS_ITEM_USER_HEAD_CLICK, item.postUuid);
                }
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (item.isAnonymous == 1 || Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(mContext, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.userUuid);
                mContext.startActivity(intent);
            }

//            else if (view.getId() == R.id.m_list_item_main_forum_food_opera_text) {// 调到点评页面
//                if (!LoginStateManager.isLogin()) {
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    mContext.startActivity(intent);
//                    return;
//                }
//                FreshNewItem item = (FreshNewItem) view.getTag();
//                if (item.isRated == 1) return;
//
//                Intent intent = new Intent(mContext, MarkingActivity.class);
//                intent.putExtra(Constants.INTENT_NEWS_ITEM, item);
//                mContext.startActivity(intent);
//            }

//            else if (view.getId() == R.id.m_list_item_main_forum_news_more_image ||
//                    view.getId() == R.id.m_list_item_main_forum_post_more_image ||
//                    view.getId() == R.id.m_list_item_main_forum_stuff_more_image ||
//                    view.getId() == R.id.m_list_item_main_forum_daily_more_image) {// 调到点评页面
//                if (!LoginStateManager.isLogin()) {
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    mContext.startActivity(intent);
//                    return;
//                }
//                FreshNewItem item = (FreshNewItem) view.getTag();
//                Intent intent = new Intent(mContext, SelectCollectOrReportActivity.class);
//                intent.putExtra(Constants.INTENT_NEWS_ITEM, item);
//                mContext.startActivity(intent);
//            }

            else if (view.getId() == R.id.m_list_item_main_forum_food_location_img
                    || view.getId() == R.id.m_list_item_main_forum_food_location) {
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
    private ArrayList<FreshNewItem> mNewsList;
    private final View.OnClickListener mItemClickListener;
    private final int[] mForumTypes = {
            R.layout.list_item_main_forum_news,
            R.layout.list_item_main_forum_post,
            R.layout.list_item_main_forum_stuff,
            R.layout.list_item_main_forum_daily,
            R.layout.list_item_main_forum_food,
            R.layout.list_item_main_forum_seek_help}; //food 推荐 stuff //买卖

    public MainNewsAdapter(Activity mContext, ArrayList<FreshNewItem> mNewsList, View.OnClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mNewsList = mNewsList;
        this.mItemClickListener = mItemClickListener;
    }

    public int getViewTypeCount() {// 返回有多少种不同类型的布局  说说   长文   宝贝  日签
        return mForumTypes.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View view = LayoutInflater.from(this.mContext).inflate(mForumTypes[viewType], null);
        switch (viewType) {
            case 0:
                holder = new ViewHolderNews(view);
                break;
            case 1:
                holder = new ViewHolderPost(view);
                break;
            case 2:
                holder = new ViewHolderStuff(view);
                break;
            case 3:
                holder = new ViewHolderDaily(view);
                break;
            case 4:
                holder = new ViewHolderFood(view);
                break;
            case 5:
                holder = new ViewHolderSeekHelp(view);
                break;
        }
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    // 这里代码虽然比较复杂  但是执行效率会好很多
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RequestOptions ops = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop();
        FreshNewItem item = mNewsList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolderNews holderNews = (ViewHolderNews) holder;
                holderNews.name.setText(item.nickname);// 姓名
                // 关注按钮
                if (item.isFollow == 0 && !Utils.isStringEquals(item.userUuid,
                        UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderNews.follow.setVisibility(View.VISIBLE);
                } else {
                    holderNews.follow.setVisibility(View.GONE);
                }
//                // 性别
//                holderNews.sex.setImageResource(item.getBigSexImageResource());

                // 时间 和 小区名称
                holderNews.time.setText(Utils.getFormattedTimeString(item.createTime));
//                holderNews.communityName.setText(item.communityName);

                // 喜欢  浏览
                holderNews.likeNumber.setText(Utils.formateNumber(item.likeCount));
                holderNews.commentNumber.setText(Utils.formateNumber(item.commentCount));
                holderNews.like.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

                holderNews.follow.setTag(item);
                holderNews.like.setTag(item);
                holderNews.head.setTag(R.id.glide_item_tag, item);
                holderNews.follow.setOnClickListener(myOnClickListener);
                holderNews.like.setOnClickListener(myOnClickListener);
                holderNews.head.setOnClickListener(myOnClickListener);
                holderNews.share.setTag(item);
                holderNews.share.setOnClickListener(myOnClickListener);


                // 匿名逻辑 主要涉及到性别  关注按钮和 头像 姓名
                if (item.isAnonymous == 1) {
                    holderNews.name.setText(mContext.getResources().getString(R.string.string_user_anonymous_alert));
                    holderNews.follow.setVisibility(View.GONE);// 匿名不需要+ta
//                    holderNews.sex.setVisibility(View.GONE);
//                    holderNews.neighbor.setVisibility(View.INVISIBLE);

                    Glide.with(mContext).load(R.mipmap.default_head_).apply(ops).into(holderNews.head);
                } else {
//                    holderNews.sex.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(ops).into(holderNews.head);
                }

                // 内容 图片  阅读全文
                if (!Utils.isStringEmpty(item.description)) {
                    holderNews.content.setMaxLines(4);
                    holderNews.content.setVisibility(View.VISIBLE);
                    if (mContext instanceof NewsSearchActivity) {
                        holderNews.content.setText(Utils.matcherSearchTitle(0xff2279AB, item.description.replace("\r", "\n"), ((NewsSearchActivity) mContext).mSearchValue));
                    } else {
                        holderNews.content.setText(item.description.replace("\r", "\n"));
                    }
                } else {
                    holderNews.content.setVisibility(View.GONE);
                }

                if (Utils.isStringEmpty(item.imgs)) {
                    holderNews.images.setVisibility(View.GONE);
                    holderNews.images.setTag("");
                } else {
                    holderNews.images.setVisibility(View.VISIBLE);
                    String oldImages = (String) holderNews.images.getTag();
                    if (!Utils.isStringEquals(oldImages, item.imgs)) { // 防止闪烁
                        holderNews.images.setTag(item.imgs);
                        holderNews.images.setDataSource(item.imgs.split(","), item);
                    }
                }
                break;
            case 1:
                ViewHolderPost holderPost = (ViewHolderPost) holder;
                holderPost.name.setText(item.nickname);// 姓名
                // 关注按钮
                if (item.isFollow == 0 && !Utils.isStringEquals(item.userUuid,
                        UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderPost.follow.setVisibility(View.VISIBLE);
                } else {
                    holderPost.follow.setVisibility(View.GONE);
                }
                // 性别
                holderPost.sex.setImageResource(item.getBigSexImageResource());

                // 时间 和 小区名称
                holderPost.time.setText(Utils.getFormattedTimeString(item.createTime));
                holderPost.communityName.setText(item.communityName);

                // 喜欢  浏览  评论数
                holderPost.likeNumber.setText(Utils.formateNumber(item.likeCount));
                holderPost.commentNumber.setText(Utils.formateNumber(item.commentCount));
                holderPost.like.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

                holderPost.follow.setTag(item);
                holderPost.like.setTag(item);
                holderPost.head.setTag(R.id.glide_item_tag, item);
                holderPost.follow.setOnClickListener(myOnClickListener);
                holderPost.like.setOnClickListener(myOnClickListener);
                holderPost.head.setOnClickListener(myOnClickListener);
                holderPost.share.setTag(item);
                holderPost.share.setOnClickListener(myOnClickListener);
                if (Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderPost.more.setClickable(false);
                } else {
                    holderPost.more.setTag(item);
                    holderPost.more.setOnClickListener(myOnClickListener);
                }
                // 邻里标签
                if (Utils.isStringEquals(item.currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid) && !Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderPost.neighbor.setVisibility(View.VISIBLE);
                } else {
                    holderPost.neighbor.setVisibility(View.INVISIBLE);
                }
                // 匿名逻辑 主要涉及到性别  关注按钮和 头像 姓名
                if (item.isAnonymous == 1) {
                    holderPost.name.setText(mContext.getResources().getString(R.string.string_user_anonymous_alert));
                    holderPost.follow.setVisibility(View.GONE);// 匿名不需要+ta
                    holderPost.sex.setVisibility(View.GONE);
                    holderPost.neighbor.setVisibility(View.INVISIBLE);
                    Glide.with(mContext).load(R.mipmap.default_head_).apply(ops).into(holderPost.head);
                } else {
                    holderPost.sex.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(ops).into(holderPost.head);
                }

                //内容可能在 title 和 description
                String content = Utils.isStringEmpty(item.title) ? item.description : item.title;
                if (!Utils.isStringEmpty(content)) {
                    holderPost.postTitle.setVisibility(View.VISIBLE);
                    if (mContext instanceof NewsSearchActivity) {
                        holderPost.postTitle.setText(Utils.matcherSearchTitle(0xff2279AB, content.replace("\r", "\n"), ((NewsSearchActivity) mContext).mSearchValue));
                    } else {
                        holderPost.postTitle.setText(content.replace("\r", "\n"));
                    }
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
                ViewHolderStuff holderStuff = (ViewHolderStuff) holder;
                holderStuff.name.setText(item.nickname);// 姓名
                // 关注按钮
                if (item.isFollow == 0 && !Utils.isStringEquals(item.userUuid,
                        UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderStuff.follow.setVisibility(View.VISIBLE);
                } else {
                    holderStuff.follow.setVisibility(View.GONE);
                }
                // 性别
                holderStuff.sex.setImageResource(item.getBigSexImageResource());

                // 时间 和 小区名称
                holderStuff.time.setText(Utils.getFormattedTimeString(item.createTime));
                holderStuff.communityName.setText(item.communityName);

                // 喜欢  浏览  评论数
                holderStuff.likeNumber.setText(Utils.formateNumber(item.likeCount));
                holderStuff.commentNumber.setText(Utils.formateNumber(item.commentCount));
                holderStuff.like.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

                holderStuff.follow.setTag(item);
                holderStuff.like.setTag(item);
                holderStuff.head.setTag(R.id.glide_item_tag, item);
                holderStuff.follow.setOnClickListener(myOnClickListener);
                holderStuff.like.setOnClickListener(myOnClickListener);
                holderStuff.head.setOnClickListener(myOnClickListener);
                holderStuff.share.setTag(item);
                holderStuff.share.setOnClickListener(myOnClickListener);
                if (Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderStuff.more.setClickable(false);
                } else {
                    holderStuff.more.setTag(item);
                    holderStuff.more.setOnClickListener(myOnClickListener);
                }
                // 邻里标签
                if (Utils.isStringEquals(item.currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid) && !Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderStuff.neighbor.setVisibility(View.VISIBLE);
                } else {
                    holderStuff.neighbor.setVisibility(View.INVISIBLE);
                }
                // 匿名逻辑 主要涉及到性别  关注按钮和 头像 姓名
                if (item.isAnonymous == 1) {
                    holderStuff.name.setText(mContext.getResources().getString(R.string.string_user_anonymous_alert));
                    holderStuff.follow.setVisibility(View.GONE);// 匿名不需要+ta
                    holderStuff.sex.setVisibility(View.GONE);
                    holderStuff.neighbor.setVisibility(View.INVISIBLE);
                    Glide.with(mContext).load(R.mipmap.default_head_).apply(ops).into(holderStuff.head);
                } else {
                    holderStuff.sex.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(ops).into(holderStuff.head);
                }

                if (!Utils.isStringEmpty(item.title)) {
                    holderStuff.content.setVisibility(View.VISIBLE);
                    holderStuff.content.setMaxLines(2);
                    if (mContext instanceof NewsSearchActivity) {
                        holderStuff.content.setText(Utils.matcherSearchTitle(0xff2279AB, item.title.replace("\r", "\n"), ((NewsSearchActivity) mContext).mSearchValue));
                    } else if (item.bizType == 1) {
                        holderStuff.content.setText("在[闲市]中，发布了一件新物品！\n" + item.title.replace("\r", "\n"));
                    } else {
                        holderStuff.content.setText(item.title.replace("\r", "\n"));
                    }
                } else {
                    holderStuff.content.setVisibility(View.GONE);
                }
                if (Utils.isStringEmpty(item.imgs)) {
                    holderStuff.images.setVisibility(View.GONE);
                    holderStuff.images.setTag("");
                } else {
                    holderStuff.images.setVisibility(View.VISIBLE);
                    String oldImages = (String) holderStuff.images.getTag();
                    if (!Utils.isStringEquals(oldImages, item.imgs)) { // 防止闪烁
                        holderStuff.images.setTag(item.imgs);
                        holderStuff.images.setDataSource(item.imgs.split(","), item);
                    }
                }
                break;
            case 3:
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
                    Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + images[0]).apply(ops).into(holderDaily.image);
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

            case 4:
                ViewHolderFood holderFood = (ViewHolderFood) holder;

                holderFood.name.setText(item.nickname);// 姓名
                // 关注按钮
                if (item.isFollow == 0 && !Utils.isStringEquals(item.userUuid,
                        UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderFood.follow.setVisibility(View.VISIBLE);
                } else {
                    holderFood.follow.setVisibility(View.GONE);
                }
                // 性别
                holderFood.sex.setImageResource(item.getBigSexImageResource());

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
                holderFood.markView.setValue(item.postScore);

                if (!Utils.isStringEmpty(item.description)) {
                    holderFood.text.setMaxLines(2);
                    holderFood.text.setVisibility(View.VISIBLE);
                    if (mContext instanceof NewsSearchActivity) {
                        holderFood.text.setText(Utils.matcherSearchTitle(0xff2279AB, item.description.replace("\r", "\n"), ((NewsSearchActivity) mContext).mSearchValue));
                    } else {
                        holderFood.text.setText(item.description.replace("\r", "\n"));
                    }
                } else {
                    holderFood.text.setVisibility(View.GONE);
                }
                // 邻里标签
                if (Utils.isStringEquals(item.currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid) && !Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    holderFood.neighbor.setVisibility(View.VISIBLE);
                } else {
                    holderFood.neighbor.setVisibility(View.INVISIBLE);
                }

                Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(ops).into(holderFood.head);

//                holderFood.likeAlert.setText("收藏");
//                if (item.isRated == 0) {
//                    holderFood.opera.setText("点评");
//                    holderFood.opera.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.img_star_halffull), null, null, null);
//                } else {
//                    holderFood.opera.setText("已点评");
//                    holderFood.opera.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.img_star_grey), null, null, null);
//                }
                holderFood.likeText.setText(Utils.formateNumber(item.dislikeCount));
                holderFood.commentText.setText(Utils.formateNumber(item.commentCount));
//                holderFood.likeImage.setImageResource(item.isDislike == 1 ? R.mipmap.btn_want_already : R.mipmap.btn_want);
//
//                holderFood.opera.setTag(item);
//                holderFood.opera.setOnClickListener(myOnClickListener);
//                holderFood.shareImage.setTag(item);
//                holderFood.shareImage.setOnClickListener(myOnClickListener);
//                holderFood.likeImage.setTag(item);
//                holderFood.likeImage.setOnClickListener(myOnClickListener);
                holderFood.follow.setTag(item);
                holderFood.head.setTag(R.id.glide_item_tag, item);
                holderFood.follow.setOnClickListener(myOnClickListener);
                holderFood.head.setOnClickListener(myOnClickListener);
                holderFood.location.setTag(item);
                holderFood.locationImg.setTag(item);
                holderFood.location.setOnClickListener(myOnClickListener);
                holderFood.locationImg.setOnClickListener(myOnClickListener);
                break;
            case 5:

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        FreshNewItem item = mNewsList.get(position);
        if (item.bizType == FreshNewItem.FRESH_ITEM_STUFF) return 2;// 宝贝
        if (item.bizType == FreshNewItem.FRESH_ITEM_ACTIVITY) return 1;// Banner
        if (item.bizType == FreshNewItem.FRESH_ITEM_POST)
            return item.renderType == 1 ? 0 : 1;// 说说和长文
        if (item.bizType == FreshNewItem.FRESH_ITEM_DAILY) return 3;// 日签
        if (item.bizType == FreshNewItem.FRESH_ITEM_NEWS) return 1;// 新闻搬运工
        if (item.bizType == FreshNewItem.FRESH_ITEM_LIVE) return 4;// 品鉴生活
        if (item.bizType == FreshNewItem.FRESH_ITEM_NOTE || item.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION)
            return 1;// 生活随手记
        if (item.bizType == FreshNewItem.FRESH_ITEM_FOOD || item.bizType == FreshNewItem.FRESH_ITEM_COMMENT)
            return 4;// 食品品鉴
        if (item.bizType == FreshNewItem.FRESH_ITEM_ANSWER)
            return 5;

        return 0;
    }

    public FreshNewItem getItem(int i) {
        return mNewsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mNewsList == null ? 0 : mNewsList.size();
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

    private void requestFollowUser(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_FOLLOW_USER_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new FollowOtherApiParameter(item.userUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestOtherInfoResponse response1 = new RequestOtherInfoResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("关注成功").show();
                    // 更新状态
                    FriendsItem friendsItem = new FriendsItem();
                    friendsItem.uuid = item.userUuid;
                    friendsItem.followType = 1;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance(mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
                        Toast.LENGTH_SHORT).show();
            }
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
                    // 更新状态
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
                    // 更新状态
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolderNews extends ViewHolder {
        @BindView(R.id.m_list_item_main_forum_news_time_text)
        TextView time;

        @BindView(R.id.m_list_item_main_forum_news_title_text)
        TextView content;
        @BindView(R.id.m_list_item_main_forum_news_images)
        ThreeGrideView images;
        @BindView(R.id.m_list_item_main_forum_news_like_image)
        ImageView like;
        //        @BindView(R.id.m_list_item_main_forum_news_head_sex)
//        ImageView sex;
        @BindView(R.id.m_list_item_main_forum_news_like_text)
        TextView likeNumber;
        @BindView(R.id.m_list_item_main_forum_news_comment_text)
        TextView commentNumber;
        @BindView(R.id.m_list_item_main_forum_news_share_image)
        ImageView share;

        @BindView(R.id.tv_buyer)
        TextView name;
        @BindView(R.id.iv_follow)
        ImageView follow;
        @BindView(R.id.riv_head_image)
        RoundImageView head;

        ViewHolderNews(View view) {
            super(view);
        }
    }

    class ViewHolderPost extends ViewHolder {
        @BindView(R.id.m_list_item_main_forum_post_time_text)
        TextView time;
        @BindView(R.id.tv_buyer)
        TextView name;
        @BindView(R.id.iv_follow)
        ImageView follow;
        @BindView(R.id.iv_neighbor)
        ImageView neighbor;
        @BindView(R.id.riv_head_image)
        RoundImageView head;
        @BindView(R.id.m_list_item_main_forum_post_like_image)
        ImageView like;
        @BindView(R.id.iv_head_sex)
        ImageView sex;
        @BindView(R.id.m_list_item_main_forum_post_like_text)
        TextView likeNumber;
        @BindView(R.id.m_list_item_main_forum_post_more_image)
        ImageView more;
        @BindView(R.id.m_list_item_main_forum_post_comment_text)
        TextView commentNumber;
        @BindView(R.id.m_list_item_main_forum_post_community_text)
        TextView communityName;
        @BindView(R.id.m_list_item_main_forum_post_title_text)
        TextView postTitle;
        @BindView(R.id.m_list_item_main_forum_post_images)
        ThreeGrideView postImage;
        @BindView(R.id.m_list_item_main_forum_post_share_image)
        ImageView share;

        ViewHolderPost(View view) {
            super(view);
        }
    }

    class ViewHolderStuff extends ViewHolder {
        @BindView(R.id.m_list_item_main_forum_stuff_time_text)
        TextView time;
        @BindView(R.id.tv_buyer)
        TextView name;
        @BindView(R.id.iv_follow)
        ImageView follow;
        @BindView(R.id.iv_neighbor)
        ImageView neighbor;
        @BindView(R.id.riv_head_image)
        RoundImageView head;
        @BindView(R.id.m_list_item_main_forum_stuff_title_text)
        TextView content;
        @BindView(R.id.m_list_item_main_forum_stuff_images)
        ThreeGrideView images;
        @BindView(R.id.m_list_item_main_forum_stuff_like_image)
        ImageView like;
        @BindView(R.id.iv_head_sex)
        ImageView sex;
        @BindView(R.id.m_list_item_main_forum_stuff_like_text)
        TextView likeNumber;
        @BindView(R.id.m_list_item_main_forum_stuff_more_image)
        ImageView more;
        @BindView(R.id.m_list_item_main_forum_stuff_comment_text)
        TextView commentNumber;
        @BindView(R.id.m_list_item_main_forum_stuff_community_text)
        TextView communityName;
        @BindView(R.id.m_list_item_main_forum_stuff_share_image)
        ImageView share;

        ViewHolderStuff(View view) {
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
        RoundImageView head;
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
        //        @BindView(R.id.m_list_item_main_forum_food_like_alert)
//        TextView likeAlert;
//        @BindView(R.id.m_list_item_main_forum_food_like_image)
//        ImageView likeImage;
        @BindView(R.id.m_list_item_main_forum_food_like_text)
        TextView likeText;
        @BindView(R.id.m_list_item_main_forum_food_comment_image)
        ImageView commentImage;
        @BindView(R.id.m_list_item_main_forum_food_comment_text)
        TextView commentText;
        @BindView(R.id.m_list_item_main_forum_food_share_image)
        ImageView shareImage;
        @BindView(R.id.riv_head_image)
        RoundImageView head;
        @BindView(R.id.iv_head_sex)
        ImageView sex;
        @BindView(R.id.tv_buyer)
        TextView name;
        @BindView(R.id.iv_neighbor)
        ImageView neighbor;
        @BindView(R.id.iv_follow)
        ImageView follow;
        @BindView(R.id.m_list_item_main_forum_food_location)
        TextView location;
        @BindView(R.id.m_list_item_main_forum_food_location_img)
        ImageView locationImg;
        @BindView(R.id.m_list_item_main_forum_food_mark_view)
        MarkView markView;
        @BindView(R.id.m_list_item_main_forum_food_text)
        TextView text;
        @BindView(R.id.m_list_item_main_forum_food_images)
        ThreeGrideView images;

        ViewHolderFood(View view) {
            super(view);
        }
    }

    class ViewHolderSeekHelp extends ViewHolder {


        ViewHolderSeekHelp(View view) {
            super(view);
        }
    }
}
