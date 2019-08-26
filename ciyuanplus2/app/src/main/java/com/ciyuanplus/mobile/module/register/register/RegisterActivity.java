package com.ciyuanplus.mobile.module.register.register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.ImageLoader;
import com.ciyuanplus.mobile.image_select.ImgSelActivity;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.FileUtils;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.module.register.agreement.AgreementActivity;
import com.ciyuanplus.mobile.module.settings.change_name.ChangeNameActivity;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.ciyuanplus.mobile.widget.NoEmojiEditText;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/9.
 * 注册页面
 * <p>
 * 2.3.3 改版之后没有临时用户， 所以不会从其他地方跳转到注册页面，
 * 进入注册页面2个方式  只有三方登录和登录页面的注册按钮。
 */

public class RegisterActivity extends MyBaseActivity implements RegisterContract.View {

    @Inject
 RegisterPresenter mPresenter;
    //    @BindView(R.id.m_register_bottom_image)
//    ImageView mRegisterBottomImage;
    @BindView(R.id.m_register_back_image)
    ImageView mRegisterBackImage;
    @BindView(R.id.m_register_head_icon)
    RoundImageView mRegisterHeadIcon;
    @BindView(R.id.m_register_camera)
    ImageView mRegisterCamera;
    @BindView(R.id.m_register_user_name)
    TextView mRegisterUserName;
    @BindView(R.id.m_register_edit_name_image)
    ImageView mRegisterEditNameImage;
    @BindView(R.id.m_register_account_view)
    EditText mRegisterAccountView;
    @BindView(R.id.m_register_verify_view)
    EditText mRegisterVerifyView;
    @BindView(R.id.m_register_send_verify_text)
    TextView mRegisterSendVerifyText;
    @BindView(R.id.m_register_password_view)
    NoEmojiEditText mRegisterPasswordView;
    @BindView(R.id.m_register_agreement_check)
    CheckBox mRegisterAgreementCheck;
    @BindView(R.id.m_register_agreement_text)
    TextView mRegisterAgreementText;
    @BindView(R.id.m_register_next)
    TextView mRegisterNext;
    @BindView(R.id.m_register_login_btn)
    TextView mRegisterLoginBtn;
    @BindView(R.id.m_register_first_lp)
    LinearLayout mRegisterFirstLp;
    private Dialog mLoadingDialog;

    private File tempFile;//选择头像相关的文件
    private String cropImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_registser);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData(getIntent());
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerRegisterPresenterComponent.builder().registerPresenterModule(new RegisterPresenterModule(this))
                .build().inject(this);

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);

        mRegisterAgreementCheck.setOnCheckedChangeListener((compoundButton, b) -> StatisticsManager.onEventInfo(StatisticsConstant.MODULE_REGISTER,
                StatisticsConstant.OP_REGISTER_AGREEMENT_CHECK_BOX_CLICK));

        mRegisterAgreementText.setText(Html.fromHtml("<u>" + getResources().getString(R.string.string_register_agreement_hint) + "</u>"));
    }

    @OnClick({R.id.m_register_camera, R.id.m_register_edit_name_image, R.id.m_register_back_image,
            R.id.m_register_agreement_text, R.id.m_register_send_verify_text, R.id.m_register_head_icon,
            R.id.m_register_next, R.id.m_register_login_btn, R.id.m_register_user_name})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_register_head_icon:
            case R.id.m_register_camera:
                Intent intent = new Intent(this, SelectImagePopActivity.class);
                this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);
                break;
            case R.id.m_register_user_name:
            case R.id.m_register_edit_name_image:
                Intent intent1 = new Intent(this, ChangeNameActivity.class);
                intent1.putExtra(Constants.INTENT_ACTIVITY_TYPE, this.getClass().getSimpleName());
                intent1.putExtra(Constants.INTENT_USER_NAME, mPresenter.getUserName());
                this.startActivityForResult(intent1, Constants.REQUEST_CODE_INPUT_USER_NAME);
                break;
            case R.id.m_register_send_verify_text:
                String accout = mRegisterAccountView.getText().toString();
                if (Utils.isStringEmpty(accout)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_phone_empty_hint)).show();
                    return;
                }
                mPresenter.sendCode(accout);
                break;
            case R.id.m_register_next:
                String accout1 = mRegisterAccountView.getText().toString();
                String mVerify = mRegisterVerifyView.getText().toString();
                String mPassword = mRegisterPasswordView.getText().toString();
                if (Utils.isStringEmpty(accout1)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_phone_empty_hint)).show();
                    return;
                }

                if (Utils.isStringEmpty(mVerify)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_verify_empty_hint)).show();
                    return;
                }
                if (Utils.isStringEmpty(mPassword)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_password_empty_hint)).show();
                    return;
                }
                if (!Utils.isValidPassword(mPassword)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_password_unformatted_hint)).show();
                    return;
                }
                if (!mRegisterAgreementCheck.isChecked()) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_agreement_unchecked_hint)).show();
                    return;
                }
                mPresenter.requestRegister(accout1, mPassword, mVerify);
                break;
            case R.id.m_register_agreement_text:
                Intent intent2 = new Intent(RegisterActivity.this, AgreementActivity.class);
                RegisterActivity.this.startActivity(intent2);
                break;
//            case R.id.m_register_login_btn:
//                Intent intent3 = new Intent(RegisterActivity.this, LoginActivity.class);
//                intent3.putExtra(Constants.INTENT_ACTIVITY_TYPE, LoginActivity.ACTIVITY_TYPE_LOGIN);
//                RegisterActivity.this.startActivity(intent3);
//                finish();
//                break;
            case R.id.m_register_back_image:
                finish();
                break;
        }
    }

    @Override
    public void setVerifyText(String string, boolean b) {
        mRegisterSendVerifyText.setText(string);
        mRegisterSendVerifyText.setClickable(b);
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
    public void changeHeadIcon(String headIconUrl) {
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + headIconUrl).apply(new RequestOptions()
                .dontAnimate().centerCrop()).into(mRegisterHeadIcon);
    }

    @Override
    public void changeName(String mNickName) {
        mRegisterUserName.setText(mNickName);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String headIconPath;
        if (requestCode == Constants.REQUEST_CODE_INPUT_USER_NAME && resultCode == RESULT_OK && data != null) {// 输入昵称
            String name = data.getStringExtra("name");// 返回的一定不是空的
            mRegisterUserName.setText(name);
            mPresenter.setNickName(name);
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_PITCURE_OPTION && resultCode == RESULT_OK && data != null) {
            // 选择头像的方式
            String option = data.getStringExtra(SelectImagePopActivity.SELECTED);
            if (Utils.isStringEquals(option, SelectImagePopActivity.LOACL_PHOTO)) {
                startSelectImage();
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
                    tempFile = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
                    FileUtils.createFile(tempFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                    startActivityForResult(cameraIntent, Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION);
                } else {
                    Toast.makeText(this, getString(R.string.image_select_open_camera_failure), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION && resultCode == RESULT_OK) {
            if (tempFile != null) {
                crop(tempFile.getAbsolutePath());// 先裁剪
            }
        } else if (requestCode == Constants.REQUEST_CODE_IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            if (!Utils.isStringEmpty(cropImagePath)) {
                Constant.imageList.clear();
                headIconPath = cropImagePath;
                mPresenter.uploadImageFile(headIconPath);
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            headIconPath = pathList.get(0);
            Constant.imageList.clear();
            mPresenter.uploadImageFile(headIconPath);
        }
    }

    // 打开图片选择activity
    private void startSelectImage() {
        ImageLoader loader = (context, path, imageView) -> Glide.with(App.mContext).load(path).apply(new RequestOptions().error(R.mipmap.imgfail)).into(imageView);
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                .multiSelect(false)
                .btnBgColor(Color.GRAY)
                .btnTextColor(Color.BLUE)
                .statusBarColor(Color.parseColor("#ffffff"))
                .title("头像")
                .titleColor(Color.BLACK)
                .titleBgColor(Color.parseColor("#ffffff"))
                .needCrop(true)
                .cropSize(2, 2, 500, 500)
                .needCamera(false)
                .maxNum(1)
                .build();
        ImgSelActivity.startActivity(this, config, Constants.REQUEST_CODE_SELECT_IMAGE);
    }

    private void crop(String imagePath) {
        File file = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");

        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, Constants.REQUEST_CODE_IMAGE_CROP_CODE);
    }
}
