package com.ciyuanplus.mobile.module.start_forum.start_news;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.module.popup.start_post_guide.StartPostGuideActivity;
import com.ciyuanplus.mobile.module.start_forum.start_note.StartNotePresenter;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.StringUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/12.
 * <p>
 * 发布和编辑新鲜事页面
 * V Drop
 */

public class StartNewsActivity extends MyBaseActivity implements StartNewsContract.View {
    @BindView(R.id.m_start_news_common_title)
    CommonTitleBar m_js_common_title;
    @BindView(R.id.m_start_news_camera_btn)
    ImageView mStartNewsCameraBtn;
    @BindView(R.id.m_start_news_anonymous_check)
    CheckBox mStartNewsAnonymousCheck;
    @BindView(R.id.m_start_news_target_zone_check)
    ImageView mStartNewsTargetZoneCheck;
    @BindView(R.id.m_start_news_bottom_lp)
    LinearLayout mStartNewsBottomLp;
    @BindView(R.id.m_start_news_title)
    EditText mStartNewsTitle;
    @BindView(R.id.m_start_news_content)
    RichTextEditor mStartNewsContent;
    @BindView(R.id.m_start_news_content_lp)
    LinearLayout mStartNewsContentLp;

    private Dialog mLoadingDialog;
    private PopupWindow mWorldPopupWindow;

    @Inject
    StartNewsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_news);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData(getIntent(), savedInstanceState);
        if (!SharedPreferencesManager.getBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_START_POST_HAS_GUIDE, false)) {
            Intent intent = new Intent(this, StartPostGuideActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPresenter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerStartNewsPresenterComponent.builder().
                startNewsPresenterModule(new StartNewsPresenterModule(this)).build().inject(this);

        m_js_common_title.setRightImage(R.mipmap.nav_icon_save);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("新闻搬运工");
        m_js_common_title.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    CommonToast.getInstance("正在发布，请勿重复点击").show();
                    return;
                }
                if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
                Utils.isStringEmpty(getTitleString());
                mPresenter.submmit();
            }
        });
        mStartNewsTitle.setFilters(new InputFilter[]{new LengthFilter(50)});

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);

        mStartNewsContent.setmImagePreViewListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                int pos = mStartNewsContent.getImageViewIndex(view);

                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, mPresenter.getEditImages());
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, pos);
                StartNewsActivity.this.startActivity(intent);
            }
        });
        mStartNewsContent.setmOnDeleteImageListener((image) -> mPresenter.removeImage(image));
        //mStartNewsContent.setMaxLength(1800);
        mStartNewsContent.setAlertText("快来分享最新的消息吧~");


        mStartNewsAnonymousCheck.setOnCheckedChangeListener((compoundButton, b) -> CommonToast.getInstance(b ? "您已选择匿名发布" : "您已取消匿名发布").show());
        mStartNewsTargetZoneCheck.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                showWorldPopUpWindow();
            }
        });
    }

    @Override
    public void updateView() {
        String title = mPresenter.mNewsItem.title;
        mStartNewsTitle.setText(title);
        mStartNewsTitle.setSelection(Utils.isStringEmpty(title) ? 0 : title.length());
        mStartNewsAnonymousCheck.setChecked(mPresenter.mNewsItem.isAnonymous == 1);
        //mStartPostAnonymousCheck.setEnabled(false);
        mStartNewsAnonymousCheck.setOnTouchListener((v, event) -> {
            CommonToast.getInstance("编辑时不能更改匿名状态").show();
            return true;
        });
        mStartNewsTargetZoneCheck.setImageResource(mPresenter.mNewsItem.isPublic == 1 ? R.mipmap.launch_icon_gongkai : R.mipmap.launch_icon_xiaoqu);

        if (!Utils.isStringEmpty(mPresenter.mNewsItem.contentText))
            showEditData(mPresenter.mNewsItem.contentText);

    }

    // 选择发布在小区还是世界的弹框
    private void showWorldPopUpWindow() {
        if (mWorldPopupWindow == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = inflater != null ? inflater.inflate(R.layout.layout_world_select_popup, null) : null;
            mWorldPopupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mWorldPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            view1.findViewById(R.id.m_world_select_popup_world).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    mPresenter.isWorldChecked = true;
                    mStartNewsTargetZoneCheck.setImageResource(R.mipmap.launch_icon_gongkai);

                }
            });
            view1.findViewById(R.id.m_world_select_popup_zone).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    mPresenter.isWorldChecked = false;
                    mStartNewsTargetZoneCheck.setImageResource(R.mipmap.launch_icon_xiaoqu);
                }
            });
        }
        mWorldPopupWindow.showAsDropDown(mStartNewsTargetZoneCheck, -Utils.dip2px(65), -Utils.dip2px(160));
        mWorldPopupWindow.setFocusable(true);
        mWorldPopupWindow.setOutsideTouchable(true);
        mWorldPopupWindow.update();
    }

    private void showEditData(final String content) {
        mStartNewsContent.post(() -> {
            mStartNewsContent.clearAllLayout();
            List<String> textList = StringUtils.cutStringByImgTag(content);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    String imagePath = StringUtils.getImgSrc(text);
//                        int width = Utils.getScreenWidth();
//                        int height = Utils.getScreenHeight();
                    mStartNewsContent.measure(0, 0);
//                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
//                        if (bitmap != null){
//                            mStartPostContent.addImageViewAtIndex(mStartPostContent.getLastIndex(), imagePath);
//                        } else {
//                            mStartPostContent.addEditTextAtIndex(mStartPostContent.getLastIndex(), text);
//                        }
                    mStartNewsContent.addImageViewAtIndex(mStartNewsContent.getLastIndex(), Constants.IMAGE_LOAD_HEADER + imagePath, "");

                } else {
                    mStartNewsContent.addEditTextAtIndex(mStartNewsContent.getLastIndex(), text);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.dealActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.imageList.clear();// 清掉当前的图片，防止下次进来还有
    }

    // 正在编辑的帖子，需要先判断一下是否需要保存  再退出

    @Override
    public void onBackPressed() {
        if (Utils.isStringEmpty(getTitleString()) && Utils.isStringEmpty(mPresenter.getEditData())) {
            finish();
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(StartNewsActivity.this);
            builder.setMessage("是否放弃发布？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            CustomDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @OnClick({R.id.m_start_news_camera_btn})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_start_news_camera_btn:
                if (mPresenter.mImagepathList.size() >= StartNotePresenter.MAX_IMAGE_SIZE) {
                    CommonToast.getInstance("最多上传" + StartNotePresenter.MAX_IMAGE_SIZE + "张图片").show();
                    return;
                }
                Intent intent = new Intent(StartNewsActivity.this, SelectImagePopActivity.class);
                StartNewsActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public String getTitleString() {
        return mStartNewsTitle.getText().toString();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
    }

    @Override
    public RichTextEditor getContentView() {
        return mStartNewsContent;
    }

    @Override
    public boolean isAnonyChecked() {
        return mStartNewsAnonymousCheck.isChecked();
    }
}
