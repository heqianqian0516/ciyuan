package com.ciyuanplus.mobile.module.forum_detail.forum_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.net.bean.CommentItem;
import com.ciyuanplus.mobile.net.bean.ReplyItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Alen
 * @date 2017/12/14
 * <p>
 * 说说   长文   宝贝   新闻  美食  生活随手记  品质生活等详情页面的 父类
 */

@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public class ForumDetailActivity extends MyBaseActivity implements ForumDetailContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.iv_share)
    ImageView mShareButton;
    @BindView(R.id.title_bar)
    protected TitleBarView mTitleBar;

    @BindView(R.id.et_input)
    EditText mInputEdit;

    @BindView(R.id.rl_input_layout)
    RelativeLayout mInputLayout;
    @BindView(R.id.ll_bottom)
    LinearLayout mCommonDetailBottomLp;
    @BindView(R.id.lv_comment)
    public ListView mCommentList;
    @BindView(R.id.m_common_detail_root_lp)
    RelativeLayout mRootLayout;

    @Inject
    public ForumDetailPresenter mPresenter;

    /**
     * 需要通过子类来进行实例化
     */
    protected View mHeadView;

    /**
     * 需要通过子类来进行实例化
     */
    protected LinearLayout mNullCommentLayout;

    @BindView(R.id.iv_like)
    protected ImageView mCommonDetailBottomOperaLp1LikeImage;

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.m_common_detail_comment_submit)
    TextView mCommonDetailCommentSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_common_detail);
        this.initView();
        mPresenter.initData(getIntent());
        this.initData();
    }

    //
    protected void initView() {
        ButterKnife.bind(this);

        DaggerForumDetailPresenterComponent.builder()
                .forumDetailPresenterModule(new ForumDetailPresenterModule(this)).build().inject(this);

        mRootLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            //现在认为只要控件将Activity向上推的高度超过了50屏幕高，就认为软键盘弹起  
            if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 50)) {
                mPresenter.resetCommentData();
                mInputEdit.setText("");
                mInputEdit.setHint("回复楼主");
            }
        });

        mInputEdit.setFilters(new InputFilter[]{new LengthFilter(120)});

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mTitleBar.setTitle("帖子详情");
        mTitleBar.setOnBackListener(v -> onBackPressed());

        mTitleBar.registerRightImage(R.mipmap.nav_icon_more, v -> {

            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                return;
            }
            if (mPresenter.isMine) {
                showNewsOperaActivity(0);
            } else {
                showNewsOperaActivity(1);
            }
        });


        mInputEdit.addTextChangedListener(new TextWatcher() {
            // 监听输入框的当前文字数量， 来变化发送按钮状态
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                   // mCommonDetailCommentSubmit.setVisibility(View.VISIBLE);
                   // mCommonDetailCommentSubmit.setEnabled(true);
                } else {
                  //  mCommonDetailCommentSubmit.setVisibility(View.GONE);
                   // mCommonDetailCommentSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mInputEdit.setOnEditorActionListener((v, actionId, event) -> {

            switch (v.getId()) {
                case R.id.et_input:
                    //发送
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        if (mPresenter.mItem == null) {
                            break;
                        }
                        if (KeyboardUtils.isSoftInputVisible(mActivity)) {
                            KeyboardUtils.hideSoftInput(v);
                        }

                        if (!LoginStateManager.isLogin()) {
                            Intent intent2 = new Intent(mActivity, LoginActivity.class);
                            startActivity(intent2);
                            break;
                        }
                        String comment = mInputEdit.getText().toString();
                        if (Utils.isStringEmpty(comment)) {
                            break;
                        }
                        mPresenter.submitCommentOrRely(comment);
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

    @Override
    public ListView getDetailCommentListView() {
        return mCommentList;
    }

    /**
     * 子类
     */
    private void initData() {

    }

    /**
     * 子类需要重写 子类需要继承  并且做自己的Top layout的内容
     */
    protected void bindTopLayout() {
    }

    /**
     * 子类需要重写
     */
    protected void showNewsOperaActivity(int i) {
    }

    /**
     * 子类需要重写
     */
    @Override
    public void updateView() {

    }

    @Override
    public void updateBottomView() {
    }

    @Override
    public void finishRefresh() {
        mSmartRefreshLayout.finishRefresh();
    }

    @Override
    public void finishLoadMore() {

        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        mSmartRefreshLayout.setEnableLoadMore(enable);
    }

    @Override
    public void stopRefreshAndLoadMore() {
        finishLoadMore();
        finishRefresh();
    }

    // 更新帖子评论页面
    @Override
    public void updateCommentView(int size) {
        if (size == 0) {
            mNullCommentLayout.setVisibility(View.VISIBLE);
        } else {
            mNullCommentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCommentText(String s) {

    }

    @Override
    public void setCommentInput(String s) {
        mInputEdit.setText("");
    }

    @OnClick({R.id.iv_share, R.id.m_common_detail_comment_submit, R.id.iv_like})
    public void onViewClicked(View view) {
        if (mPresenter == null || mPresenter.mItem == null) {
            return;
        }
        super.onViewClicked(view);
        switch (view.getId()) {

            case R.id.iv_like:
                if (mPresenter.mItem == null) {
                    break;
                }
                if (!LoginStateManager.isLogin()) {
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    startActivity(intent1);
                    break;
        }

                if (mPresenter.mItem.isLike == 1) {
                    mPresenter.cancelLikePost();
                } else {
                    mPresenter.likePost();
                }

                break;
//

            case R.id.iv_share:
                Intent intent = new Intent(this, ShareNewsPopupActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, mPresenter.mItem);
                this.startActivity(intent);
                break;

            case R.id.m_common_detail_comment_submit:
                if (mPresenter.mItem == null) {
                    break;
                }

                if (!LoginStateManager.isLogin()) {
                    Intent intent2 = new Intent(this, LoginActivity.class);
                    startActivity(intent2);
                    break;
                }
                String comment = mInputEdit.getText().toString();
                if (Utils.isStringEmpty(comment)) {
                    break;
                }
                mPresenter.submitCommentOrRely(comment);
                HideKeyboard(mInputEdit);
                break;
            default:
                break;
        }
    }


    public void tryDelete(CommentItem item) {
        mPresenter.mTryDeleteComment = item;
        // 先保存一下
        Intent intent = new Intent(this, SelectDeleteCommentActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, SelectDeleteCommentActivity.DELETE_COMENT);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_DELETE_COMMENT);
    }

    public void tryDelete(ReplyItem item) {
        mPresenter.mTryDeleteReply = item;// 先保存一下
        Intent intent = new Intent(this, SelectDeleteCommentActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, SelectDeleteCommentActivity.DELETE_REPLY);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_DELETE_COMMENT);
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

    private void setReplyCommentUser(CommentItem item) {
        mInputEdit.setHint("回复 " + item.nickname);
        mInputEdit.setFocusable(true);
        mInputEdit.requestFocus();
        mInputLayout.setVisibility(View.VISIBLE);
        ShowKeyboard(mInputEdit);
        mPresenter.mReplyCommentId = item.commentUuid;
        mPresenter.mTargetCommentId = item.commentUuid;
        mPresenter.mReplyToUserId = item.userUuid;
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
        mInputEdit.setHint("回复 " + item.sendNickname);
        mInputEdit.setFocusable(true);
        mInputEdit.requestFocus();
        mInputLayout.setVisibility(View.VISIBLE);
        ShowKeyboard(mInputEdit);
        mPresenter.mReplyCommentId = item.parentCommentUuid;
        mPresenter.mTargetCommentId = item.commentUuid;
        mPresenter.mReplyToUserId = item.sendUserUuid;
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

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.hideSoftInput(this);
            mPresenter.resetCommentData();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        mPresenter.requestForumDetail();
        mPresenter.requestPostComments(true);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {

        mPresenter.requestPostComments(false);
    }


}
