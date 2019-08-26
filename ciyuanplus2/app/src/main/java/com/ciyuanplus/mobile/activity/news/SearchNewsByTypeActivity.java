//package tech.milin.social.Activity.News;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.litesuits.http.exception.HttpException;
//import com.litesuits.http.request.StringRequest;
//import com.litesuits.http.request.param.HttpMethods;
//import com.litesuits.http.response.Response;
//
//import java.util.ArrayList;
//
//
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import tech.milin.social.Adapter.MainNewsAdapter;
//import tech.milin.social.Manager.EventCenterManager;
//import tech.milin.social.Manager.UserInfoData;
//import tech.milin.social.Module.ForumDetail.DailyDetail.DailyDetailActivity;
//import tech.milin.social.Module.ForumDetail.NoteDetail.NoteDetailActivity;
//import tech.milin.social.Module.ForumDetail.PostDetail.PostDetailActivity;
//import tech.milin.social.MyBaseActivity;
//import tech.milin.social.Net.ApiContant;
//import tech.milin.social.Net.Bean.FreshNewItem;
//import tech.milin.social.Net.Bean.FriendsItem;
//import tech.milin.social.Net.Bean.PostTypeItem;
//import tech.milin.social.Net.LiteHttpManager;
//import tech.milin.social.Net.MyHttpListener;
//import tech.milin.social.Net.Parameter.RequestCommunityPostsApiParameter;
//import tech.milin.social.Net.Parameter.RequestWorldPostsApiParameter;
//import tech.milin.social.Net.Response.RequestFreshNewsResponse;
//import tech.milin.social.Net.ResponseData;
//import tech.milin.social.R;
//import tech.milin.social.Statistics.StatisticsConstant;
//import tech.milin.social.Statistics.StatisticsManager;
//import tech.milin.social.Utils.Constants;
//import tech.milin.social.Utils.Utils;
//import tech.milin.social.Widget.CommonToast;
//import tech.milin.social.pulltorefresh.XListView;
//
///**
// * Created by Alen on 2017/11/3.
// * 选择帖子类型之后的  跳转页面
// *
// * Drop
// */
//
//public class SearchNewsByTypeActivity extends MyBaseActivity implements XListView.IXListViewListener, EventCenterManager.OnHandleEventListener {
//    @BindView(R.id.m_search_news_by_type_tab_all_title)
//    TextView mSearchNewsByTypeTabAllTitle;
//    @BindView(R.id.m_search_news_by_type_tab_all_title_lp)
//    RelativeLayout mSearchNewsByTypeTabAllTitleLp;
//    @BindView(R.id.m_search_news_by_type_tab_mine_title)
//    TextView mSearchNewsByTypeTabMineTitle;
//    @BindView(R.id.m_search_news_by_type_tab_mine_title_lp)
//    RelativeLayout mSearchNewsByTypeTabMineTitleLp;
//    @BindView(R.id.m_search_news_by_type_tab_title_lp)
//    LinearLayout mSearchNewsByTypeTabTitleLp;
//    @BindView(R.id.m_search_news_by_type_back_image)
//    ImageView mSearchNewsByTypeBackImage;
//    @BindView(R.id.m_search_news_by_type_top_lp)
//    RelativeLayout mSearchNewsByTypeTopLp;
//    @BindView(R.id.m_search_news_by_type_list)
//    XListView mSearchNewsByTypeList;
//    @BindView(R.id.m_imageView)
//    ImageView mImageView;
//    @BindView(R.id.m_search_news_by_type_null_lp)
//    LinearLayout mSearchNewsByTypeNullLp;
//    private int mNextPage = 0;
//    private int PAGE_SIZE = 20;
//
//    private ArrayList<FreshNewItem> mNewsList = new ArrayList<FreshNewItem>();
//    private MainNewsAdapter mNewsAdapter;
//
//    private String mLastId = "";
//    private PostTypeItem mPostTypeItem;
//    private int mSearchType = -1;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.setContentView(R.layout.activity_search_news_by_type);
//        mPostTypeItem = (PostTypeItem) getIntent().getSerializableExtra(Constants.INTENT_POST_TYPE_ITEM);
//        this.initView();
//    }
//
//    private void initView() {
//        mUnbinder = ButterKnife.bind(this);
//
//        //mNewsAdapter = new MainNewsAdapter(this, mNewsList);
//        //mSearchNewsByTypeList.setAdapter(mNewsAdapter);
//        mSearchNewsByTypeList.setPullRefreshEnable(true);
//        mSearchNewsByTypeList.setPullLoadEnable(true);
//        mSearchNewsByTypeList.setXListViewListener(this);
//        mSearchNewsByTypeList.setOnItemClickListener((adapterView, view, i, l) -> {
//            if (l == -1) {
//                return;
//            }
//            int postion = (int) l;
//            FreshNewItem item = mNewsAdapter.getItem(postion);
//            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, StatisticsConstant.OP_SEARCH_NEWS_ITEM_ENTER_CLICK, item.postUuid);
//
//            if (item.renderType == 1) {
//                Intent intent = new Intent(SearchNewsByTypeActivity.this, NoteDetailActivity.class);
//                intent.putExtra(Constants.INTENT_HIDE_TAG, true); // tag列表页面进入到详情页面  不能显示tag标签
//                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
//                startActivity(intent);
//            } else if(item .bizType == 3) {
//                Intent intent = new Intent(SearchNewsByTypeActivity.this, DailyDetailActivity.class);
//                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(SearchNewsByTypeActivity.this, PostDetailActivity.class);
//                intent.putExtra(Constants.INTENT_HIDE_TAG, true);
//                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
//                startActivity(intent);
//            }
//
//        });
//
//
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
//        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
//
//        if (!Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().currentCommunityName))
//            mSearchNewsByTypeTabMineTitle.setText(UserInfoData.getInstance().getUserInfoItem().currentCommunityName);// 设置下默认的小区
//        switchTabSelect(0);
//    }
//
//    private void doSearchQuery() {
//        if (mSearchType == 0) {
//            requestWorldPost();
//        } else if (mSearchType == 1) {
//            requestZonePost();
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        mNextPage = 0;
//        mLastId = "";
//        mNewsList.clear();
//        doSearchQuery();
//    }
//
//    @Override
//   public void onLoadMore() {
//        doSearchQuery();
//    }
//
//    private void requestZonePost() {
//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
//                + ApiContant.REQUEST_COMMUNITY_POST_URL);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new RequestCommunityPostsApiParameter(UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid,
//                "", mNextPage + "", PAGE_SIZE + "", mLastId, mPostTypeItem.typeId).getRequestBody());
//        postRequest.setHttpListener(new MyHttpListener<String>() {
//            @Override
//            public void onSuccess(String s, Response<String> response) {
//                super.onSuccess(s, response);
//                // 必须要加的
//                mSearchNewsByTypeList.stopLoadMore();
//                mSearchNewsByTypeList.stopRefresh();
//
//                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
//                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
//                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
//                    if (mNextPage == 0) mNewsList.clear();
//                    for (int i = 0; i < response1.freshNewsListItem.list.length; i++) {
//                        mNewsList.add(response1.freshNewsListItem.list[i]);
//                    }
//                    mLastId = mNewsList.get(mNewsList.size() - 1).id;
//                    mNextPage++;
//                }
//                mNewsAdapter.notifyDataSetChanged();
//                updateList();
//            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                // 必须要加的
//                mSearchNewsByTypeList.stopLoadMore();
//                mSearchNewsByTypeList.stopRefresh();
//                super.onFailure(e, response);
//            }
//        });
//        LiteHttpManager.getInstance().executeAsync(postRequest);
//    }
//
//    private void updateList() {
//        if (mNewsList.size() > 0) {
//            mSearchNewsByTypeNullLp.setVisibility(View.GONE);
//            mSearchNewsByTypeList.setVisibility(View.VISIBLE);
//            mNewsAdapter.notifyDataSetChanged();
//        } else {
//            CommonToast.getInstance("未搜索到内容").show();
//            mSearchNewsByTypeNullLp.setVisibility(View.VISIBLE);
//            mSearchNewsByTypeList.setVisibility(View.GONE);
//        }
//    }
//
//    private void requestWorldPost() {
//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
//                + ApiContant.REQUEST_WORLD_POST_URL);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new RequestWorldPostsApiParameter("", mNextPage + "", PAGE_SIZE + "", mLastId, mPostTypeItem.typeId, "").getRequestBody());
//        postRequest.setHttpListener(new MyHttpListener<String>() {
//            @Override
//            public void onSuccess(String s, Response<String> response) {
//                super.onSuccess(s, response);
//                // 必须要加的
//                mSearchNewsByTypeList.stopLoadMore();
//                mSearchNewsByTypeList.stopRefresh();
//
//                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
//                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
//                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
//                    if (mNextPage == 0) mNewsList.clear();
//                    for (int i = 0; i < response1.freshNewsListItem.list.length; i++) {
//                        mNewsList.add(response1.freshNewsListItem.list[i]);
//                    }
//                    mLastId = mNewsList.get(mNewsList.size() - 1).id;
//                    mNextPage++;
//                }
//                mNewsAdapter.notifyDataSetChanged();
//                updateList();
//            }
//
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                // 必须要加的
//                mSearchNewsByTypeList.stopLoadMore();
//                mSearchNewsByTypeList.stopRefresh();
//                super.onFailure(e, response);
//
//            }
//        });
//        LiteHttpManager.getInstance().executeAsync(postRequest);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
//        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
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
//            for (int i = 0; i < mNewsList.size(); i++) {
//                if (Utils.isStringEquals(uuid, mNewsList.get(i).postUuid)) {
//                    mNewsList.get(i).isLike = isLike;
//                    mNewsList.get(i).likeCount = likeCount;
//                    mNewsAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mNewsList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
//                    mNewsList.remove(i);
//                    mNewsAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
//            FriendsItem friendsItem = (FriendsItem) eventMessage.mObject;
//            for (int i = 0; i < mNewsList.size(); i++) {
//                if (Utils.isStringEquals(friendsItem.uuid, mNewsList.get(i).userUuid)) {
//                    mNewsList.get(i).isFollow = friendsItem.followType;
//                }
//            }
//            mNewsAdapter.notifyDataSetChanged();
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mNewsList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
//                    mNewsList.get(i).commentCount++;
//                    mNewsAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mNewsList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
//                    mNewsList.get(i).commentCount--;
//                    mNewsAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mNewsList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
//                    mNewsList.get(i).browseCount++;
//                    mNewsAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }
//        }
//    }
//
//    /**
//     * 切换tab
//     */
//    private void switchTabSelect(int type) {
//        if (mSearchType == type) return;
//        mSearchType = type;
//        if (type == 0) {
//            mNextPage = 0;
//            mNewsList.clear();
//            mLastId = "";
//            requestWorldPost();
//            mSearchNewsByTypeTabAllTitleLp.setSelected(true);
//            mSearchNewsByTypeTabMineTitleLp.setSelected(false);
//        } else {
//            mNextPage = 0;
//            mLastId = "";
//            mNewsList.clear();
//            requestZonePost();
//            mSearchNewsByTypeTabMineTitleLp.setSelected(true);
//            mSearchNewsByTypeTabAllTitleLp.setSelected(false);
//        }
//    }
//
//    @OnClick({R.id.m_search_news_by_type_tab_all_title_lp, R.id.m_search_news_by_type_tab_mine_title_lp, R.id.m_search_news_by_type_back_image})
//    public void onViewClicked(View view) {
//        super.onViewClicked(view);
//        switch (view.getId()) {
//            case R.id.m_search_news_by_type_tab_all_title_lp:
//                switchTabSelect(0);
//                break;
//            case R.id.m_search_news_by_type_tab_mine_title_lp:
//                switchTabSelect(1);
//                break;
//            case R.id.m_search_news_by_type_back_image:
//                onBackPressed();
//                break;
//        }
//    }
//}
