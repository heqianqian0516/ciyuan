package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 * 消息列表
 */

class SystemMessageAdapter extends BaseAdapter {
    private final Context mContext;

    public SystemMessageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_system_message, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_system_message_icon)
        RoundImageView icon;
        @BindView(R.id.m_list_item_system_message_hot_tag)
        ImageView hotTag;
        @BindView(R.id.m_list_item_system_message_opera_lp)
        RelativeLayout operaLayout;
        @BindView(R.id.m_list_item_system_message_system_lp)
        LinearLayout systemLayout;
        @BindView(R.id.m_list_item_system_message_name)
        TextView name;
        @BindView(R.id.m_list_item_system_message_time)
        TextView time;
        @BindView(R.id.m_list_item_system_message_opera)
        TextView opera;
        @BindView(R.id.m_list_item_system_message_content)
        TextView content;
        @BindView(R.id.m_list_item_system_message_time2)
        TextView time2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}