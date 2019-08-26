package com.ciyuanplus.mobile.module.popup.share_invite_popup;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/11/14.
 * <p>
 * 邀请好友弹出页面
 */

public class ShareInvitePopupActivity extends MyBaseActivity implements UMShareListener {
    @BindView(R.id.m_share_invite_pop_up_wei_circle)
    LinearLayout mShareInvitePopUpWeiCircle;
    @BindView(R.id.m_share_invite_pop_up_wei_chat)
    LinearLayout mShareInvitePopUpWeiChat;
    @BindView(R.id.m_share_invite_pop_up_qq)
    LinearLayout mShareInvitePopUpQq;
    @BindView(R.id.m_share_invite_pop_up_contacts)
    LinearLayout mShareInvitePopUpContacts;
    @BindView(R.id.m_share_invite_pop_up_cancel)
    TextView mShareInvitePopUpCancel;
    private String url = " ";
    private String icon = " ";
    private String title = " ";
    private String desc = " ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_share_invite_pop_up);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        url = getIntent().getStringExtra("url");
        icon = getIntent().getStringExtra("icon");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");

        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        this.runOnUiThread(() -> {
            CommonToast.getInstance(getResources().getString(R.string.string_share_success_alert), Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_OK, null);
            finish();
        });
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        this.runOnUiThread(() -> {
            CommonToast.getInstance(getResources().getString(R.string.string_share_fail_alert), Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_CANCELED, null);
            finish();
        });
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        this.runOnUiThread(() -> {
            CommonToast.getInstance(getResources().getString(R.string.string_share_cancel_alert), Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_CANCELED, null);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_CONTACTS && resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
            cursor.moveToFirst();
            String num = this.getContactPhone(cursor);
            //打开短信app
            Uri uri = Uri.parse("smsto:" + num);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            sendIntent.putExtra("sms_body", desc + "\n" + url);
            startActivity(sendIntent);

            this.setResult(RESULT_OK, null);
            finish();
        }
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

    @OnClick({R.id.m_share_invite_pop_up_wei_circle, R.id.m_share_invite_pop_up_wei_chat, R.id.m_share_invite_pop_up_qq,
            R.id.m_share_invite_pop_up_contacts, R.id.m_share_invite_pop_up_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        UMWeb web = new UMWeb(url);
        switch (view.getId()) {
            case R.id.m_share_invite_pop_up_wei_circle:
                web.setTitle(title);//标题
                web.setThumb(Utils.isStringEmpty(icon) ? new UMImage(ShareInvitePopupActivity.this, R.mipmap.ic_launcher) : new UMImage(ShareInvitePopupActivity.this, Constants.IMAGE_LOAD_HEADER + icon));
                web.setDescription(desc);//描述
                new ShareAction(ShareInvitePopupActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(web)
                        .setCallback(ShareInvitePopupActivity.this)
                        .share();
                break;
            case R.id.m_share_invite_pop_up_wei_chat:
                web.setTitle(title);//标题
                web.setThumb(Utils.isStringEmpty(icon) ? new UMImage(ShareInvitePopupActivity.this, R.mipmap.ic_launcher) : new UMImage(ShareInvitePopupActivity.this, Constants.IMAGE_LOAD_HEADER + icon));
                web.setDescription(desc);//描述
                new ShareAction(ShareInvitePopupActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .setCallback(ShareInvitePopupActivity.this)
                        .share();
                break;
            case R.id.m_share_invite_pop_up_qq:
                web.setTitle(title);//标题
                web.setThumb(Utils.isStringEmpty(icon) ? new UMImage(ShareInvitePopupActivity.this, R.mipmap.ic_launcher) : new UMImage(ShareInvitePopupActivity.this, Constants.IMAGE_LOAD_HEADER + icon));
                web.setDescription(desc);//描述
                new ShareAction(ShareInvitePopupActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(web)
                        .setCallback(ShareInvitePopupActivity.this)
                        .share();
                break;
            case R.id.m_share_invite_pop_up_contacts:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_CONTACTS);
                break;
            case R.id.m_share_invite_pop_up_cancel:
                ShareInvitePopupActivity.this.setResult(RESULT_CANCELED, null);
                ShareInvitePopupActivity.this.finish();
                break;
        }
    }
}
