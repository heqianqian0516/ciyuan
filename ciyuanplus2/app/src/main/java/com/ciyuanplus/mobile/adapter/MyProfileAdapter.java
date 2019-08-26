package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.bean.MyProfileItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 我的个人中心和设置的Adapter
 * 由于第一个版本是放在一起的 所有使用同一个Adapter
 */

public class MyProfileAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<MyProfileItem> mlist;

    public MyProfileAdapter(Context mContext, ArrayList<MyProfileItem> list) {
        this.mContext = mContext;
        mlist = list;
    }

    //刷新数据


    @Override
    public int getCount() {
        return mlist == null ? 0 : mlist.size();
    }

    @Override
    public MyProfileItem getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        MyProfileItem item = mlist.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_my_profile, null);
            holder = new ViewHolder(convertView);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(item.name);
        if (Utils.isStringEquals(item.value, MyProfileItem.TYPE_PHONE)
                && !Utils.isStringEquals("绑定手机", item.value)) {
            holder.profile.setText(item.value);
        }

        if (Utils.isStringEquals(item.type, MyProfileItem.TYPE_HEAD)) {//如果是头像编辑， 则显示头像
            holder.profile.setVisibility(View.INVISIBLE);
            holder.headIcon.setVisibility(View.VISIBLE);
            holder.mLayoutAccountManage.setVisibility(View.GONE);

            if (mContext != null && !Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().photo)) { // 照片如果为空 使用默认头像
                Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + UserInfoData.getInstance().getUserInfoItem().photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                        .dontAnimate().centerCrop()).into(holder.headIcon);
//                ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + UserInfoData.getInstance().getUserInfoItem().photo,
//                        holder.headIcon, new ImageSize(75, 75));
            }
        } else if (Utils.isStringEquals(item.type, MyProfileItem.TYPE_NAME)
                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_SEX)
                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_BIRTHDAY)
                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_PHONE)
                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_IDENTIFY)
                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_CACHE)
                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_UPDATE)
                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_SIGN)) {//
            holder.profile.setVisibility(View.VISIBLE);
            holder.headIcon.setVisibility(View.GONE);
            holder.mLayoutAccountManage.setVisibility(View.GONE);
            if (!Utils.isStringEmpty(item.value)) holder.profile.setText(item.value);
        } else if (Utils.isStringEquals(item.type, MyProfileItem.TYPE_ACCOUNT_MANAGE)) {
            holder.mLayoutAccountManage.setVisibility(View.VISIBLE);
            holder.profile.setVisibility(View.INVISIBLE);
            holder.headIcon.setVisibility(View.GONE);
            holder.bindQQ.setImageResource(item.getIsQQBind() == 1 ? R.drawable.icon_setting_bind_qq : R.drawable.icon_setting_unbind_qq);
            holder.bindWeibo.setImageResource(item.getIsWeiBoBind() == 1 ? R.drawable.icon_setting_bind_weibo : R.drawable.icon_setting_unbind_weibo);
            holder.bindWeixin.setImageResource(item.getIsWXBind() == 1 ? R.drawable.icon_setting_bind_weixin : R.drawable.icon_setting_unbind_weixin);

        } else {
            holder.profile.setVisibility(View.INVISIBLE);
            holder.headIcon.setVisibility(View.GONE);
            holder.mLayoutAccountManage.setVisibility(View.GONE);
        }

        //下面是分区的判断
//        if (Utils.isStringEquals(item.type, MyProfileItem.TYPE_PASSWORD)
//                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_ABOUT)
//                || Utils.isStringEquals(item.type, MyProfileItem.TYPE_BIRTHDAY)) {
////            holder.bigDivider.setVisibility(View.VISIBLE);
//            holder.smallDivider.setVisibility(View.GONE);
//        } else {
////            holder.bigDivider.setVisibility(View.GONE);
//            holder.smallDivider.setVisibility(View.VISIBLE);
//        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_my_profile_head_image)
        RoundImageView headIcon;
        @BindView(R.id.m_list_item_my_profile_name)
        TextView name;
        @BindView(R.id.m_list_item_my_profile_text)
        TextView profile;
        @BindView(R.id.m_list_item_my_profile_small_divider)
        View smallDivider;
        @BindView(R.id.m_list_item_my_profile_big_divider)
        View bigDivider;
        @BindView(R.id.ll_account_manage)
        LinearLayout mLayoutAccountManage;
        @BindView(R.id.iv_bind_qq)
        ImageView bindQQ;
        @BindView(R.id.iv_bind_weixin)
        ImageView bindWeixin;
        @BindView(R.id.iv_bind_weibo)
        ImageView bindWeibo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
