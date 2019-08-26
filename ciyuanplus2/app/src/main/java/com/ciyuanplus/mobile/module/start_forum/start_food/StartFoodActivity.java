package com.ciyuanplus.mobile.module.start_forum.start_food;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.module.start_forum.select_post_location.SelectPostLocationActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.ciyuanplus.mobile.widget.MarkView;
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
 * Created by Alen on 2018/1/3.
 */

public class StartFoodActivity extends MyBaseActivity implements StartFoodContract.View {
    @BindView(R.id.m_start_food_add_image)
    ImageView mStartFoodAddImage;
    @BindView(R.id.m_start_food_location_name)
    TextView mStartFoodLocationName;
    @BindView(R.id.m_start_food_bottom_lp)
    LinearLayout mStartFoodBottomLp;
    @BindView(R.id.m_start_mark_view)
    MarkView mStartFoodMarkView;
    @BindView(R.id.m_start_food_content)
    RichTextEditor mStartFoodContent;
    @BindView(R.id.m_start_food_common_title)
    CommonTitleBar commonTitleBar;
    @BindView(R.id.m_start_food_location_img)
    ImageView mStartFoodLocationImg;

    private Dialog mLoadingDialog;
    @Inject
 StartFoodPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_food);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData(getIntent(), savedInstanceState);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerStartFoodPresenterComponent.builder().
                startFoodPresenterModule(new StartFoodPresenterModule(this)).build().inject(this);

        commonTitleBar.setLeftImage(R.mipmap.nav_icon_back);
        commonTitleBar.setRightImage(R.mipmap.nav_icon_save);
        commonTitleBar.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                onBackPressed();
            }
        });
        commonTitleBar.setCenterText("发布点评");
        commonTitleBar.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    CommonToast.getInstance("正在发布，请勿重复点击").show();
                    return;
                }

                if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();

                mPresenter.submmit();
            }
        });

        mStartFoodContent.setmImagePreViewListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                int pos = mStartFoodContent.getImageViewIndex(view);
                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, mPresenter.getEditImages());
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, pos);
                StartFoodActivity.this.startActivity(intent);
            }
        });
        mStartFoodContent.setmOnDeleteImageListener((image) -> mPresenter.removeImage(image));
        //mStartFoodContent.setMaxLength(1800);
        mStartFoodContent.setAlertText("请分享您的体验，图文越多，越受关注哦~");

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPresenter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @OnClick({R.id.m_start_food_add_image, R.id.m_start_food_location_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.m_start_food_add_image:
                if (mPresenter.mImagepathList.size() >= StartFoodPresenter.MAX_IMAGE_SIZE) {
                    CommonToast.getInstance("最多上传" + StartFoodPresenter.MAX_IMAGE_SIZE + "张图片").show();
                    return;
                }
                Intent intent = new Intent(StartFoodActivity.this, SelectImagePopActivity.class);
                StartFoodActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);

                break;
            case R.id.m_start_food_location_name:
                if (mPresenter.isEdit){
                    CommonToast.getInstance("编辑时不能更改").show();
                    return;
                }
                Intent intent2 = new Intent(StartFoodActivity.this, SelectPostLocationActivity.class);
                intent2.putExtra(Constants.INTENT_ACTIVITY_TYPE, this.getClass().getSimpleName());
                StartFoodActivity.this.startActivityForResult(intent2, Constants.REQUEST_CODE_SELECT_POST_LOCATION);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        String content = mPresenter.getEditData();// 需要先上传完成之后才能搞
        if (Utils.isStringEmpty(content)) {
            finish();
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(StartFoodActivity.this);
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
    public void showEditData(final String content) {
        mStartFoodContent.post(() -> {
            mStartFoodContent.clearAllLayout();
            List<String> textList = StringUtils.cutStringByImgTag(content);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    String imagePath = StringUtils.getImgSrc(text);
//                        int width = Utils.getScreenWidth();
//                        int height = Utils.getScreenHeight();
                    mStartFoodContent.measure(0, 0);
//                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
//                        if (bitmap != null){
//                            mStartFoodContent.addImageViewAtIndex(mStartFoodContent.getLastIndex(), imagePath);
//                        } else {
//                            mStartFoodContent.addEditTextAtIndex(mStartFoodContent.getLastIndex(), text);
//                        }
                    mStartFoodContent.addImageViewAtIndex(mStartFoodContent.getLastIndex(), Constants.IMAGE_LOAD_HEADER + imagePath, "");

                } else {
                    mStartFoodContent.addEditTextAtIndex(mStartFoodContent.getLastIndex(), text);
                }
            }
        });
    }

    @Override
    public int getScore() {
        return (int) mStartFoodMarkView.getValue();
    }

    @Override
    public void setScore(double score) {// 编辑模式
        mStartFoodMarkView.setEditMode();
        mStartFoodMarkView.setValue(score);
    }

    @Override
    public RichTextEditor getContextView() {
        return mStartFoodContent;
    }

    @Override
    public void showLoadingDialog() {
        if (isFinishing()) return;
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();

    }

    @Override
    public void changePlaceName(String placeName) {
        mStartFoodLocationName.setText(placeName);
        mStartFoodLocationName.setTextColor(0xfff0590e);
        mStartFoodLocationImg.setImageResource(R.mipmap.btn_release_location_already);
    }

    @Override
    public void dismissLoadingDialog() {
        if (isFinishing()) return;
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }
}
