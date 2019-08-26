//package tech.milin.social.Activity.Mine;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.litesuits.http.exception.HttpException;
//import com.litesuits.http.request.StringRequest;
//import com.litesuits.http.request.param.CacheMode;
//import com.litesuits.http.request.param.HttpMethods;
//import com.litesuits.http.response.Response;
//
//import java.util.ArrayList;
//import java.util.concurrent.TimeUnit;
//
//
//import butterknife.ButterKnife;
//import tech.milin.social.Module.ForumDetail.DailyDetail.DailyDetailActivity;
//import tech.milin.social.Module.ForumDetail.NoteDetail.NoteDetailActivity;
//import tech.milin.social.Module.ForumDetail.PostDetail.PostDetailActivity;
//import tech.milin.social.Adapter.MineNewsAdapter;
//import tech.milin.social.Interface.MyOnClickListener;
//import tech.milin.social.Manager.CacheManager;
//import tech.milin.social.Manager.EventCenterManager;
//import tech.milin.social.Manager.LoginStateManager;
//import tech.milin.social.Manager.SharedPreferencesManager;
//import tech.milin.social.MyBaseActivity;
//import tech.milin.social.Net.ApiContant;
//import tech.milin.social.Net.Bean.FreshNewItem;
//import tech.milin.social.Net.LiteHttpManager;
//import tech.milin.social.Net.MyHttpListener;
//import tech.milin.social.Net.Parameter.RequestFreshNewsApiParameter;
//import tech.milin.social.Net.Response.RequestFreshNewsResponse;
//import tech.milin.social.Net.ResponseData;
//import tech.milin.social.R;
//import tech.milin.social.Statistics.StatisticsConstant;
//import tech.milin.social.Statistics.StatisticsManager;
//import tech.milin.social.Utils.Constants;
//import tech.milin.social.Utils.Utils;
//import tech.milin.social.Widget.CommonTitleBar;
//import tech.milin.social.Widget.CommonToast;
//import tech.milin.social.pulltorefresh.XListView;
//
///**
// * Created by Alen on 2017/8/11.
// * 我的新鲜事   drop
// */
//
//public class MineNewsActivity extends MyBaseActivity implements EventCenterManager.OnHandleEventListener, XListView.IXListViewListener {
//    private static int PAGE_SIZE = 10;
//    @BindView(R.id.m_main_mine_news_list)
//    XListView mMainMineNewsList;
//    @BindView(R.id.m_main_mine_null_lp)
//    LinearLayout mMainMineNullLp;
//    @BindView(R.id.m_mine_news_common_title)
//    CommonTitleBar m_js_common_title;
//
//    private MineNewsAdapter mMyPublishAdapter;
//    private ArrayList<FreshNewItem> mMyPublishList = new ArrayList<FreshNewItem>();
//    private int mNextPage = 0;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.setContentView(R.layout.activity_mine_news);
//        this.initView();
//        requestMyPublish();
//    }
//
//    private void initView() {
//        mUnbinder = ButterKnife.bind(this);
//        m_js_common_title.setCenterText("我的新鲜事");
//        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
//            @Override
//            public void performRealClick(View view) {
//                onBackPressed();
//            }
//        });
//
//        mMainMineNewsList.setPullLoadEnable(true);
//        mMainMineNewsList.setPullRefreshEnable(true);
//        mMainMineNewsList.setXListViewListener(this);
//
//        mMyPublishAdapter = new MineNewsAdapter(this, mMyPublishList, null);
//        mMainMineNewsList.setAdapter(mMyPublishAdapter);
//        mMainMineNewsList.setOnItemClickListener((parent, view, position, id) -> {
//            if (id == -1) {
//                return;
//            }
//            int postion = (int) id;
//            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE, StatisticsConstant.OP_MINE_LIKE_LIST_ITEM_CLICK, mMyPublishAdapter.getItem(postion).postUuid);
//
//            FreshNewItem item = mMyPublishAdapter.getItem(postion);
//            if (item.renderType == 1) {
//                Intent intent = new Intent(MineNewsActivity.this, NoteDetailActivity.class);
//                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
//                startActivity(intent);
//            } else if(item.bizType == 3) {
//                Intent intent = new Intent(MineNewsActivity.this, DailyDetailActivity.class);
//                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(MineNewsActivity.this, PostDetailActivity.class);
//                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
//                startActivity(intent);
//            }
//        });
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST, this);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST, this);
//
//        super.onDestroy();
//    }
//
//    @Override
//    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
//        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT) {
//            String uuid;
//            int isLike;
//            int likeCount;
//            if (eventMessage.mObject instanceof FreshNewItem) {
//                FreshNewItem item = (FreshNewItem) eventMessage.mObject;
//                uuid = item.postUuid;
//                isLike = item.isLike;
//                likeCount = item.likeCount;
//            } else {
//                FreshNewItem item = (FreshNewItem) eventMessage.mObject;
//                uuid = item.postUuid;
//                isLike = item.isLike;
//                likeCount = item.likeCount;
//            }
//            for (int i = 0; i < mMyPublishList.size(); i++) {
//                if (Utils.isStringEquals(uuid, mMyPublishList.get(i).postUuid)) {
//                    mMyPublishList.get(i).isLike = isLike;
//                    mMyPublishList.get(i).likeCount = likeCount;
//                    mMyPublishAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mMyPublishList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
//                    mMyPublishList.remove(i);
//                    mMyPublishAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mMyPublishList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
//                    mMyPublishList.get(i).commentCount++;
//                    mMyPublishAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mMyPublishList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
//                    mMyPublishList.get(i).commentCount--;
//                    mMyPublishAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mMyPublishList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
//                    mMyPublishList.get(i).browseCount++;
//                    mMyPublishAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST) {
//            mNextPage = 0;
//            requestMyPublish();
//        }
//    }
//
//    // 获取我的发布
//    private void requestMyPublish() {
//        if (!LoginStateManager.isLogin()) {
//            mMainMineNewsList.stopLoadMore();
//            mMainMineNewsList.stopRefresh();
//            return;
//        }
//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
//                + ApiContant.REQUEST_GET_MY_PUBLISH_POST_URL);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new RequestFreshNewsApiParameter(mNextPage + "", PAGE_SIZE + "").getRequestBody());
//        // 设置缓存
//        postRequest.setCacheMode(CacheMode.NetFirst);
//        postRequest.setCacheExpire(-1, TimeUnit.SECONDS);
//        postRequest.setCacheDir(CacheManager.getInstance().getSettleDirectory());
//        postRequest.setCacheKey(null);
//
//        String sessionKey = SharedPreferencesManager.getString(
//                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
//        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
//        postRequest.setHttpListener(new MyHttpListener<String>() {
//            @Override
//            public void onSuccess(String s, Response<String> response) {
//                super.onSuccess(s, response);
//                // 必须要加的
//                mMainMineNewsList.stopLoadMore();
//                mMainMineNewsList.stopRefresh();
//
//                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
//                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
//                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
//                    if (mNextPage == 0) mMyPublishList.clear();
//                    for (int i = 0; i < response1.freshNewsListItem.list.length; i++) {
//                        mMyPublishList.add(response1.freshNewsListItem.list[i]);
//                    }
//                    mMyPublishAdapter.notifyDataSetChanged();
//                    mNextPage++;
//                    updateListView();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                // 必须要加的
//                mMainMineNewsList.stopLoadMore();
//                mMainMineNewsList.stopRefresh();
//                if (response.isCacheHit()) { // 中了缓存就不要在折腾了，直接加载数据
//                    String s = response.getRequest().getDataParser().getData();
//                    RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
//                    if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
//                        if (mNextPage == 0) mMyPublishList.clear();
//                        for (int i = 0; i < response1.freshNewsListItem.list.length; i++) {
//                            mMyPublishList.add(response1.freshNewsListItem.list[i]);
//                        }
//                        mMyPublishAdapter.notifyDataSetChanged();
//                        mNextPage++;
//                    }
//                    updateListView();
//                }
//                super.onFailure(e, response);
////
////                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
////                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        LiteHttpManager.getInstance().executeAsync(postRequest);
//    }
//
//    // 刷新下面的 listview
//    private void updateListView() {
//        if (mMyPublishList.size() > 0) {
//            this.mMainMineNewsList.setVisibility(View.VISIBLE);
//            mMainMineNullLp.setVisibility(View.GONE);
//        } else {
//            this.mMainMineNewsList.setVisibility(View.GONE);
//            mMainMineNullLp.setVisibility(View.VISIBLE);
//        }
//    }
//
//    // 上啦刷新
//    @Override
//    public void onRefresh() {
//        mNextPage = 0;
//        requestMyPublish();
//    }
//
//    // 下拉加载
//    @Override
//   public void onLoadMore() {
//        requestMyPublish();
//    }
//}
