package com.ciyuanplus.mobile.activity.chat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/20.
 */

public class InviteActivity extends MyBaseActivity implements UMShareListener {

    @BindView(R.id.m_invite_contact_lp)
    RelativeLayout mInviteContactLp;
    @BindView(R.id.m_invite_weichat_circle_lp)
    RelativeLayout mInviteWeichatCircleLp;
    @BindView(R.id.m_invite_weichat_lp)
    RelativeLayout mInviteWeichatLp;
    @BindView(R.id.m_invite_qq_lp)
    RelativeLayout mInviteQqLp;
    @BindView(R.id.m_invite_common_title)
    CommonTitleBar m_js_common_title;

    private String mShareTitle;
    private String mShareLink;
    private String mShareContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_invite);

        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        String DEFAULT_SHARE_TITLE = "次元PLUS";
        mShareTitle = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_SHARE_TITLE, DEFAULT_SHARE_TITLE);
        String DEFAULT_SHARE_LINK = "http://m.liangzi365.com";
        mShareLink = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_SHARE_LINK, DEFAULT_SHARE_LINK);
        String DEFAULT_SHARE_CONTENT = "嗨，快来次元PLUS与我一起加入社区大Party，发现更多好玩的事儿!";
        mShareContent = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_SHARE_CONTENT, DEFAULT_SHARE_CONTENT);

        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        m_js_common_title.setCenterText("邀请好友");
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_CONTACTS && resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
            cursor.moveToFirst();
            String num = this.getContactPhone(cursor);
            //打开短信app
            Uri uri = Uri.parse("smsto:" + num);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            sendIntent.putExtra("sms_body", mShareContent + "\n" + mShareLink);
            startActivity(sendIntent);

        }
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

    private String getContactPhone(Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            if (phone != null && phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
//                  switch (phone_type) {//此处请看下方注释
//                  case 2:
//                      result = phoneNumber;
//                      break;
//
//                  default:
//                      break;
//                  }
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result;
    }

    @OnClick({R.id.m_invite_contact_lp, R.id.m_invite_weichat_circle_lp, R.id.m_invite_weichat_lp, R.id.m_invite_qq_lp})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        UMWeb web = new UMWeb(mShareLink);
        switch (view.getId()) {
            case R.id.m_invite_contact_lp:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_CONTACTS);
                break;
            case R.id.m_invite_weichat_circle_lp:
                web.setTitle(mShareTitle);//标题
                web.setThumb(new UMImage(InviteActivity.this, R.mipmap.share_img));
                web.setDescription(mShareContent);//描述
                new ShareAction(InviteActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(web)
                        .setCallback(InviteActivity.this)
                        .share();
                break;
            case R.id.m_invite_weichat_lp:
                web.setTitle(mShareTitle);//标题
                web.setThumb(new UMImage(InviteActivity.this, R.mipmap.share_img));
                web.setDescription(mShareContent);//描述
                new ShareAction(InviteActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .setCallback(InviteActivity.this)
                        .share();
                break;
            case R.id.m_invite_qq_lp:
                web.setTitle(mShareTitle);//标题
                web.setThumb(new UMImage(InviteActivity.this, R.mipmap.share_img));
                web.setDescription(mShareContent);//描述
                new ShareAction(InviteActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(web)
                        .setCallback(InviteActivity.this)
                        .share();
                break;
        }
    }
}
