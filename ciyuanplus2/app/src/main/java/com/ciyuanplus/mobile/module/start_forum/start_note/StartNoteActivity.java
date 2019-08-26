package com.ciyuanplus.mobile.module.start_forum.start_note;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.StringUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Alen on 2017/8/17.
 * 生活随手记
 */

public class StartNoteActivity extends MyBaseActivity implements StartNoteContract.View {
    @BindView(R.id.m_start_note_add_image)
    ImageView mStartNoteAddImage;
    @BindView(R.id.m_start_note_anonymous_check)
    CheckBox mStartNoteAnonymousCheck;
    @BindView(R.id.m_start_note_bottom_lp)
    LinearLayout mStartNoteBottomLp;
    @BindView(R.id.m_start_note_content)
    RichTextEditor mStartNoteContent;
    @BindView(R.id.m_start_note_common_title)
    CommonTitleBar m_js_common_title;
    @BindView(R.id.m_start_note_target_zone_check)
    ImageView mStartNoteTargetZoneCheck;

    private Dialog mLoadingDialog;
    private PopupWindow mWorldPopupWindow;

    @Inject
 StartNotePresenter mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_note);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();

        mPresenter.initData(getIntent(), savedInstanceState);

        // 为了让编辑的时候进来   不弹出这个提示，放在这里设置listener，暂时先这么改
        mStartNoteAnonymousCheck.setOnCheckedChangeListener((compoundButton, b) -> CommonToast.getInstance(b ? "您已选择匿名发布" : "您已取消匿名发布").show());
        if (!SharedPreferencesManager.getBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_START_POST_HAS_GUIDE, false)) {
            Intent intent = new Intent(this, StartPostGuideActivity.class);
            startActivity(intent);
        }
        mStartNoteTargetZoneCheck.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                showWorldPopUpWindow();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mPresenter != null) mPresenter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerStartNotePresenterComponent.builder().startNotePresenterModule(new StartNotePresenterModule(this))
                .build().inject(this);

        m_js_common_title.setLeftImage(R.mipmap.nav_icon_back);
        m_js_common_title.setRightImage(R.mipmap.nav_icon_save);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();

        m_js_common_title.setCenterText("发布杂谈");
        mStartNoteContent.setAlertText("请输入您的杂谈内容～");


        m_js_common_title.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    CommonToast.getInstance("正在发布，请勿重复点击").show();
                    return;
                }

                if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();

                String content = getEditData();
                if (Utils.isStringEmpty(content)) {
                    CommonToast.getInstance(getResources().getString(R.string.start_post_content_empty_alert)).show();
                    if (mLoadingDialog != null && mLoadingDialog.isShowing())
                        mLoadingDialog.dismiss();
                    return;
                }


                mPresenter.requestPost(ApiContant.REQUEST_ADD_MY_NEWS_URL, 13);


            }
        });

        mStartNoteContent.setmImagePreViewListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                int pos = mStartNoteContent.getImageViewIndex(view);

                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, mPresenter.getEditImages(mStartNoteContent));
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, pos);
                StartNoteActivity.this.startActivity(intent);
            }
        });
        mStartNoteContent.setmOnDeleteImageListener((image) -> mPresenter.removeImage(image));
        //mStartNoteContent.setMaxLength(1800);


        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void updateView() {
        mStartNoteAnonymousCheck.setChecked(mPresenter.mNewsItem.isAnonymous == 1);
        //mStartNoteAnonymousCheck.setEnabled(false);
        mStartNoteAnonymousCheck.setOnTouchListener((v, event) -> {
            CommonToast.getInstance("编辑时不能更改匿名状态").show();
            return true;
        });

        mStartNoteTargetZoneCheck.setImageResource(mPresenter.mNewsItem.isPublic == 1 ? R.mipmap.launch_icon_gongkai : R.mipmap.launch_icon_xiaoqu);

        if (!Utils.isStringEmpty(mPresenter.mNewsItem.contentText))
            showEditData(mPresenter.mNewsItem.contentText);

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

    // 选择发布在小区还是世界的弹框
    private void showWorldPopUpWindow() {
        if (mWorldPopupWindow == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = inflater != null ? inflater.inflate(R.layout.layout_world_select_popup, null) : null;
            mWorldPopupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mWorldPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            view1.findViewById( R.id.m_world_select_popup_world).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    mPresenter.isWorldChecked = true;
                    mStartNoteTargetZoneCheck.setImageResource(R.mipmap.launch_icon_gongkai);


                }
            });
            view1.findViewById( R.id.m_world_select_popup_zone).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    mPresenter.isWorldChecked = false;
                    mStartNoteTargetZoneCheck.setImageResource(R.mipmap.launch_icon_xiaoqu);

                }
            });
        }
        mWorldPopupWindow.showAsDropDown(mStartNoteTargetZoneCheck, -Utils.dip2px(65), -Utils.dip2px(160));
        mWorldPopupWindow.setFocusable(true);
        mWorldPopupWindow.setOutsideTouchable(true);
        mWorldPopupWindow.update();
    }

    @Override
    public void onBackPressed() {
        if (Utils.isStringEmpty(getEditData())) {
            finish();
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(StartNoteActivity.this);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.dealActivityResult(requestCode, resultCode, data, mStartNoteContent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStartNoteContent.clearAllLayout();
        Constant.imageList.clear();// 清掉当前的图片，防止下次进来还有
    }

    @OnClick({R.id.m_start_note_add_image})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_start_note_add_image:
                if (mPresenter.mImagePathList.size() >= StartNotePresenter.MAX_IMAGE_SIZE) {
                    CommonToast.getInstance("最多上传" + StartNotePresenter.MAX_IMAGE_SIZE + "张图片").show();
                    return;
                }
                Intent intent = new Intent(StartNoteActivity.this, SelectImagePopActivity.class);
                StartNoteActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    private void showEditData(final String content) {
        mStartNoteContent.post(() -> {
            mStartNoteContent.clearAllLayout();
            List<String> textList = StringUtils.cutStringByImgTag(content);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    String imagePath = StringUtils.getImgSrc(text);
//                        int width = Utils.getScreenWidth();
//                        int height = Utils.getScreenHeight();
                    mStartNoteContent.measure(0, 0);
//                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
//                        if (bitmap != null){
//                            mStartNoteContent.addImageViewAtIndex(mStartNoteContent.getLastIndex(), imagePath);
//                        } else {
//                            mStartNoteContent.addEditTextAtIndex(mStartNoteContent.getLastIndex(), text);
//                        }
                    mStartNoteContent.addImageViewAtIndex(mStartNoteContent.getLastIndex(), Constants.IMAGE_LOAD_HEADER + imagePath, "");

                } else {
                    mStartNoteContent.addEditTextAtIndex(mStartNoteContent.getLastIndex(), text);
                }
            }
        });

    }

    @Override
    public String getEditData() {
        List<RichTextEditor.EditData> editList = mStartNoteContent.buildEditData();
        StringBuilder content = new StringBuilder();
        mPresenter.mUploadUrls.clear();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(mPresenter.mUploadMap.get(itemData.imagePath)).append("\"/>");
                mPresenter.mUploadUrls.add(mPresenter.mUploadMap.get(itemData.imagePath));
            }
        }
        return content.toString();
    }

    @Override
    public boolean isAnonyChecked() {
        return mStartNoteAnonymousCheck.isChecked();
    }
}
