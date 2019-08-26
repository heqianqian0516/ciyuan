package com.ciyuanplus.mobile.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.HomeADBean;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Alen on 2017/10/31.
 */

public class ShareNewsPopupActivity extends MyBaseActivity implements UMShareListener {

    private static final String TAG = "ShareNewsPopupActivity";
    private FreshNewItem mNewsItem;
    private String url = " ";
    private String icon = " ";
    private String title = " ";
    private String desc = " ";
    private boolean isStuff = false;
    private HomeADBean.DataBean homeAdbean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_share_popup);

        mNewsItem = (FreshNewItem) getIntent().getSerializableExtra(Constants.INTENT_NEWS_ITEM);
        int bizType = mNewsItem.bizType;

        this.initView();
        initData();
    }

    private void initData() {
        String url_header = " ";
        if (Utils.isDebug()) {
            url_header =
                    ApiContant.WEB_DETAIL_VIEW_URL + "cyplus-share/";
        } else {
            url_header = ApiContant.WEB_DETAIL_VIEW_URL + "cyplus-share/";

        }
        if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_STUFF) {// 宝贝
            title = mNewsItem.nickname + "在次元PLUS的闲市中发布了一件宝贝";
            desc = mNewsItem.title;
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "stuff.html?postUuid=" + mNewsItem.postUuid;
            return;
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_ACTIVITY) { // 活动
            title = mNewsItem.title;
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url ="http://app.mi.com/details?id=com.asdfghjjkk.superiordiaryokdsakd";
          // url = url_header + "event.html?uuid=" + homeAdbean.getUuid();
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_POST) { // 说说和长文
            if (mNewsItem.renderType == 1) { // 说说
                if (!Utils.isStringEmpty(mNewsItem.contentText)) {
                    title = mNewsItem.contentText.length() > 24 ? mNewsItem.contentText.substring(0, 24) : mNewsItem.contentText;
                    desc = mNewsItem.contentText.length() > 40 ? mNewsItem.contentText.substring(0, 40) : mNewsItem.contentText;
                } else {
                    title = mNewsItem.isAnonymous == 1 ? "神秘人" : mNewsItem.nickname + "发布了一条说说";
                    desc = " ";
                }
                if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                    String[] images = mNewsItem.imgs.split(",");
                    icon = Constants.IMAGE_LOAD_HEADER + images[0];
                } else {
                    icon = " ";
                }
                url = url_header + "post.html?postUuid=" + mNewsItem.postUuid;
            } else if (mNewsItem.bizType == 0) {//长文
                title = mNewsItem.title;
                if (!Utils.isStringEmpty(mNewsItem.description)) {
                    desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
                } else {
                    desc = " ";
                }
                if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                    String[] images = mNewsItem.imgs.split(",");
                    icon = Constants.IMAGE_LOAD_HEADER + images[0];
                } else {
                    icon = " ";
                }
                url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
            }
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_DAILY) {
            // 暂时没有这种类型的分享
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_NEWS) {//新闻搬运工  需要改地址
            title = mNewsItem.title;
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_LIVE) {// 品质体验 需要改地址
            title = mNewsItem.nickname + "发布了一条品质体验";
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_NOTE) {//
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                title = mNewsItem.description.length() > 24 ? mNewsItem.description.substring(0, 24) : mNewsItem.description;
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                title = mNewsItem.isAnonymous == 1 ? "神秘人" : mNewsItem.nickname + "发布了一条生活随手记";
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {//
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                title = mNewsItem.description.length() > 24 ? mNewsItem.description.substring(0, 24) : mNewsItem.description;
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                title = mNewsItem.isAnonymous == 1 ? "神秘人" : mNewsItem.nickname + "发布了一条新鲜事";
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_FOOD) {
            title = mNewsItem.nickname + "发布了一条美食品鉴";
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        } else if (mNewsItem.bizType == FreshNewItem.FRESH_ITEM_COMMENT) {
            title = mNewsItem.nickname + "发布了一条点评";
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        } else if (mNewsItem.bizType == 14) {
            title = mNewsItem.title;
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        } else if (mNewsItem.bizType == 4) {//长文
            title = mNewsItem.title;
            if (!Utils.isStringEmpty(mNewsItem.description)) {
                desc = mNewsItem.description.length() > 40 ? mNewsItem.description.substring(0, 40) : mNewsItem.description;
            } else {
                desc = " ";
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                String[] images = mNewsItem.imgs.split(",");
                icon = Constants.IMAGE_LOAD_HEADER + images[0];
            } else {
                icon = " ";
            }
            url = url_header + "postLong.html?postUuid=" + mNewsItem.postUuid;
        }

        Log.e(TAG, "initData: " + url);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        this.runOnUiThread(() -> CommonToast.getInstance(getResources().getString(R.string.string_share_success_alert), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        this.runOnUiThread(() -> CommonToast.getInstance(getResources().getString(R.string.string_share_fail_alert), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        this.runOnUiThread(() -> CommonToast.getInstance(getResources().getString(R.string.string_share_cancel_alert), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.m_share_pop_up_wei_circle, R.id.m_share_pop_up_wei_chat,
            R.id.m_share_pop_up_qq, R.id.m_share_pop_up_weibo, R.id.m_share_pop_up_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        UMWeb web = new UMWeb(url);
        switch (view.getId()) {
            case R.id.m_share_pop_up_wei_circle:
                web.setTitle(title);//标题
                web.setThumb(Utils.isStringEmpty(icon) ? new UMImage(ShareNewsPopupActivity.this, R.mipmap.ic_launcher) : new UMImage(ShareNewsPopupActivity.this, icon));
                web.setDescription(desc);//描述
                new ShareAction(ShareNewsPopupActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(web)
                        .setCallback(ShareNewsPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_wei_chat:
                web.setTitle(title);//标题
                web.setThumb(Utils.isStringEmpty(icon) ? new UMImage(ShareNewsPopupActivity.this, R.mipmap.ic_launcher) : new UMImage(ShareNewsPopupActivity.this, icon));
                web.setDescription(desc);//描述
                new ShareAction(ShareNewsPopupActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .setCallback(ShareNewsPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_qq:
                web.setTitle(title);//标题
                web.setThumb(Utils.isStringEmpty(icon) ? new UMImage(ShareNewsPopupActivity.this, R.mipmap.ic_launcher) : new UMImage(ShareNewsPopupActivity.this, icon));
                web.setDescription(desc);//描述
                new ShareAction(ShareNewsPopupActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(web)
                        .setCallback(ShareNewsPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_weibo:
                web.setTitle(title);//标题
                web.setThumb(Utils.isStringEmpty(icon) ? new UMImage(ShareNewsPopupActivity.this, R.mipmap.ic_launcher) : new UMImage(ShareNewsPopupActivity.this, icon));
                web.setDescription(desc);//描述
                new ShareAction(ShareNewsPopupActivity.this).setPlatform(SHARE_MEDIA.SINA)
                        .withMedia(web)
                        .setCallback(ShareNewsPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_cancel:
                ShareNewsPopupActivity.this.finish();
                break;
        }
    }
}
