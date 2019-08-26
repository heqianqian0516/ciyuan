package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.NoticeItem;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 */

public class UserNoticesAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<NoticeItem> mList;

    public UserNoticesAdapter(Context mContext, ArrayList<NoticeItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }


    public NoticeItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        NoticeItem item = mList.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_user_notice, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop()).into(holder.icon);
//        ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + item.photo, holder.icon, new ImageSize(75, 75));
        holder.name.setText(item.nickname);
        holder.detail.setText(item.contentText);
        holder.time.setText(Utils.getFormattedTimeString(item.createTime));

        holder.sex.setImageResource(UserInfoItem.getSexImageResource(item.sex));


        if (item.isAnonymous == 1) {
            holder.sex.setVisibility(View.GONE);
            holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            holder.sex.setVisibility(View.VISIBLE);
            if (item.neibType == 1)
                holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.mipmap.icon_neighbor_label), null);
            else holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if (item.noticeType == 1 || item.noticeType == 5 || item.noticeType == 6)
            holder.detail.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_comment), null, null, null);
        else if (item.noticeType == 2 || item.noticeType == 9)
            holder.detail.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_like), null, null, null);
        else if (item.noticeType == 4)
            holder.detail.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.message_icon_details_follow), null, null, null);

        // 由于评论和赞 使用同一个noticeType， 所以只能使用字符串进行判断， 技术方案 request by 冠华
        if (!Utils.isStringEmpty(item.contentText) && item.contentText.startsWith("赞同了您的评论"))
            holder.detail.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_like), null, null, null);

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_user_notice_icon)
        RoundImageView icon;
        @BindView(R.id.m_list_item_user_notice_name)
        TextView name;
        @BindView(R.id.m_list_item_user_notice_detail)
        TextView detail;
        @BindView(R.id.m_list_item_user_notice_sex)
        ImageView sex;
        @BindView(R.id.m_list_item_user_notice_time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}