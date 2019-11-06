package com.ciyuanplus.mobile.module.video.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.adapter.VideoCommentAdapter;
import com.ciyuanplus.mobile.adapter.VideoDetailAdapter;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;

import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.video.RecommendData;
import com.ciyuanplus.mobile.module.video.videoutils.DouyinLayoutManager;
import com.ciyuanplus.mobile.module.video.videoutils.OnViewPagerListener;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.CommentItem;
import com.ciyuanplus.mobile.net.bean.ReplyItem;
import com.ciyuanplus.mobile.net.parameter.AddCommentApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestPostCommentApiParameter;
import com.ciyuanplus.mobile.net.response.RequestPostCommentResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * 视频详情页
 * adapter 布局video_detail_item_list.xml
 * */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public class VideoDetailsActivity extends MyBaseActivity  {

    @BindView(R.id.video_detail_recy)
    RecyclerView videoDetailRecy;
    private List<RecommendData.DataBean.ListBean> list;
    private DouyinLayoutManager douyinLayoutManager;
    private VideoDetailAdapter videoDetailAdapter;
    private Dialog dialog;
    private ListView lvComment;
    private ImageView commentSubmit;
    private EditText edInput;
    private RelativeLayout relativeComment;
    /**
     * 需要通过子类来进行实例化
     */
    protected LinearLayout mNullCommentLayout;
    private RelativeLayout mInputLayout;
    private final ArrayList<CommentItem> mCommentList = new ArrayList<>();
    private int mNextPage = 0;
    private final int PAGE_SIZE = 20;
    // 防止连续请求  数据重复
    private boolean mIsRequestComment = false;
    private VideoCommentAdapter videoCommentAdapter;
    private String postUuid;
    public String mReplyCommentId;
    public String mTargetCommentId;
    public String mReplyToUserId;
    private String userUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
        ButterKnife.bind(this);


        initView();

        initListener();
    }


    private void initView() {
      /*  DaggerForumDetailPresenterComponent.builder()
                .forumDetailPresenterModule(new ForumDetailPresenterModule(this)).build().inject(this);
*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        list = (List<RecommendData.DataBean.ListBean>) bundle.getSerializable("list");

        douyinLayoutManager = new DouyinLayoutManager(this, OrientationHelper.VERTICAL, false);
        videoDetailAdapter = new VideoDetailAdapter(list, this);
        videoDetailRecy.setLayoutManager(douyinLayoutManager);
        videoDetailRecy.setAdapter(videoDetailAdapter);
        dialog = new Dialog(VideoDetailsActivity.this, R.style.DialogTheme);
        videoDetailAdapter.setOnCommentClickListener(new VideoDetailAdapter.OnCommentClickListener() {
            @Override
            public void onCommnetListener(int position) {
                // CommonToast.getInstance("点击评论了111").show();
                View view = View.inflate(VideoDetailsActivity.this, R.layout.commnet_item_diaglog, null);
                dialog.setContentView(view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();
                getshoud(2);
                commentSubmit = view.findViewById(R.id.comment_submit);
                edInput = view.findViewById(R.id.et_input);
                lvComment = view.findViewById(R.id.lv_comment);
                relativeComment = view.findViewById(R.id.common_detail_root_lp);
                mInputLayout = view.findViewById(R.id.rl_input_layout);
                postUuid = list.get(position).getPostUuid();
                userUuid = list.get(position).getUserUuid();
                requestComment();
                videoCommentAdapter = new VideoCommentAdapter(VideoDetailsActivity.this,mCommentList,list.get(position).getBizType());
                lvComment.setAdapter(videoCommentAdapter);
                commentSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = edInput.getText().toString();
                        if (Utils.isStringEmpty(comment)) {
                            return;
                        }
                        submitCommentOrRely(comment);

                        HideKeyboard(edInput);
                    }
                });
                relativeComment.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                    //现在认为只要控件将Activity向上推的高度超过了50屏幕高，就认为软键盘弹起  
                    if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 50)) {
                         resetCommentData();
                        edInput.setText("");
                        edInput.setHint("回复楼主");
                    }
                });
                edInput.setFilters(new InputFilter[]{new LengthFilter(120)});
                edInput.setOnEditorActionListener((v, actionId, event) -> {

                    switch (v.getId()) {
                        case R.id.et_input:
                            //发送
                            if (actionId == EditorInfo.IME_ACTION_SEND) {
                               /* if (mPresenter.mItem == null) {
                                    break;
                                }*/
                                if (KeyboardUtils.isSoftInputVisible(mActivity)) {
                                    KeyboardUtils.hideSoftInput(v);
                                }

                                if (!LoginStateManager.isLogin()) {
                                    Intent intent2 = new Intent(mActivity, LoginActivity.class);
                                    startActivity(intent2);
                                    break;
                                }
                                String comment = edInput.getText().toString();
                                if (Utils.isStringEmpty(comment)) {
                                    break;
                                }
                                 submitCommentOrRely(comment);
                            }
                            break;
                        default:
                            Logger.d("还好");
                            break;
                    }

                    return false;
                });
                bindTopLayout();
            }

        });

    }

    private void resetCommentData() {

            mReplyCommentId = "";
            mReplyToUserId = "";
            mTargetCommentId = "";

    }

    private void requestComment() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestPostCommentApiParameter(mNextPage + "", PAGE_SIZE + "", postUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mIsRequestComment = false;

                // 必须要加的
              //  mView.stopRefreshAndLoadMore();


                RequestPostCommentResponse response1 = new RequestPostCommentResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    if (mNextPage == 0) {
                        mCommentList.clear();
                    }
                    mNextPage++;
                  //  mView.setLoadMoreEnable(response1.commentListItem.list.length >= pageSize);
                    Collections.addAll(mCommentList, response1.commentListItem.list);

                    videoCommentAdapter.notifyDataSetChanged();
                   // mView.updateCommentView(mCommentList.size());
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
                mIsRequestComment = false;

               // mView.stopRefreshAndLoadMore();
                super.onFailure(e, response);
            }
        });
        mIsRequestComment = true;
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    /**
     * 回复楼猪帖子
     */

    public void submitCommentOrRely(String comment) {
        if (Utils.isStringEmpty(mReplyCommentId)) {
            submitComment(comment);
        } else {
            submitReply(comment, mReplyCommentId, mTargetCommentId, mReplyToUserId);
        }
    }

    private void submitReply(String comment, String commentId, String targetCommentId, String toUserUuid) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddCommentApiParameter(userUuid, commentId, targetCommentId, comment, "2", toUserUuid).getRequestBody());
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
                    CommonToast.getInstance("评论成功").show();
                   /* mView.setCommentInput("");
                    mItem.commentCount++;
                    mView.setCommentText(Utils.formateNumber(mItem.commentCount));*/
                    mNextPage = 0;
                    requestPostComments(PAGE_SIZE);// 成功之后需要刷新评论列表
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, userUuid));
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void getshoud(int bottom) {
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65，根据实际情况调整
        if (bottom == 1) {
            p.height = (int) (d.getHeight() * 0.2);

        } else {
            p.height = (int) (d.getHeight() * 0.65);

        }

        dialogWindow.setAttributes(p);
    }

    private void initListener() {
        douyinLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
               // Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean bottom) {
               // Log.e(TAG, "选择位置:" + position + " 下一页:" + bottom);

                playVideo(0);
            }
        });
    }
    /**
     *释放视频
     * @param index
     */
    private void releaseVideo(int index) {
        View itemView = videoDetailRecy.getChildAt(index);
        StandardGSYVideoPlayer standardGSYVideoPlayer = itemView.findViewById(R.id.video_player);
        standardGSYVideoPlayer.release();
    }
    /**
     * 播放视频
     * @param position
     */
    private void playVideo(int position) {
        View itemView = videoDetailRecy.getChildAt(position);
        StandardGSYVideoPlayer standardGSYVideoPlayer = itemView.findViewById(R.id.video_player);
        ImageView imgPlay = itemView.findViewById(R.id.img_play);
        ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        standardGSYVideoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onStartPrepared(String url, Object... objects) {
                super.onStartPrepared(url, objects);
                imgThumb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                imgThumb.setVisibility(View.GONE);
            }


        });


        standardGSYVideoPlayer.startPlayLogic();


        //播放按钮控制
        imgPlay.setOnClickListener(v -> {



        });
    }


    // 当评论被点击时候的回调
    public void repleyComment(CommentItem item) {
        if (!LoginStateManager.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        setReplyCommentUser(item);
    }
    // 提交帖子的评论内容
    private void submitComment(String comments) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddCommentApiParameter(postUuid, "", "", comments, "1",userUuid ).getRequestBody());
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
                    CommonToast.getInstance("评论成功").show();
                   /* mView.setCommentInput("");
                    mItem.commentCount++;*/

                   // mView.setCommentText(Utils.formateNumber(mItem.commentCount));
                    mNextPage = 0;
                    requestPostComments(PAGE_SIZE);// 成功之后需要刷新评论列表
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, userUuid));
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
    private void requestPostComments(int pageSize) {
        if (mIsRequestComment) {
            return;
        }

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestPostCommentApiParameter(mNextPage + "", pageSize + "", userUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mIsRequestComment = false;

                // 必须要加的
                //mView.stopRefreshAndLoadMore();


                RequestPostCommentResponse response1 = new RequestPostCommentResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    if (mNextPage == 0) {
                        mCommentList.clear();
                    }
                    mNextPage++;
                    //mView.setLoadMoreEnable(response1.commentListItem.list.length >= pageSize);
                    Collections.addAll(mCommentList, response1.commentListItem.list);

                    videoCommentAdapter.notifyDataSetChanged();
                  //  mView.updateCommentView(mCommentList.size());
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
                mIsRequestComment = false;

               // mView.stopRefreshAndLoadMore();
                super.onFailure(e, response);
            }
        });
        mIsRequestComment = true;
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
    private void setReplyCommentUser(CommentItem item) {
        edInput.setHint("回复 " + item.nickname);
        edInput.setFocusable(true);
        edInput.requestFocus();
        edInput.setVisibility(View.VISIBLE);
        ShowKeyboard(edInput);
        /*mPresenter.mReplyCommentId = item.commentUuid;
        mPresenter.mTargetCommentId = item.commentUuid;
        mPresenter.mReplyToUserId = item.userUuid;*/
    }
    /* 当回复被点击时候的回调 */
    public void replyReply(ReplyItem item) {
        if (!LoginStateManager.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        setReplyUser(item);
    }

    private void setReplyUser(ReplyItem item) {
        edInput.setHint("回复 " + item.sendNickname);
        edInput.setFocusable(true);
        edInput.requestFocus();
        mInputLayout.setVisibility(View.VISIBLE);
        ShowKeyboard(edInput);
       /* mPresenter.mReplyCommentId = item.parentCommentUuid;
        mPresenter.mTargetCommentId = item.commentUuid;
        mPresenter.mReplyToUserId = item.sendUserUuid;*/
    }

    /**
     * 子类
     */
    private void initData() {

    }


    public void tryDelete(CommentItem item) {
       // mPresenter.mTryDeleteComment = item;
        // 先保存一下
        Intent intent = new Intent(this, SelectDeleteCommentActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, SelectDeleteCommentActivity.DELETE_COMENT);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_DELETE_COMMENT);
    }

    public void tryDelete(ReplyItem item) {
        //mPresenter.mTryDeleteReply = item;// 先保存一下
        Intent intent = new Intent(this, SelectDeleteCommentActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, SelectDeleteCommentActivity.DELETE_REPLY);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_DELETE_COMMENT);
    }
    /**
     * 子类需要重写 子类需要继承  并且做自己的Top layout的内容
     */
    protected void bindTopLayout() {
    }
    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    //显示虚拟键盘
    private static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }
}
