package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.MyMessagesItem;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 * 消息列表
 */

public class MyMessageAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<MyMessagesItem> mlist;

    public MyMessageAdapter(Context mContext, ArrayList<MyMessagesItem> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist == null ? 0 : mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        MyMessagesItem item = mlist.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_my_message, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (item.number > 0) {
            holder.number.setText(String.format("%d", item.number));
            holder.number.setVisibility(View.VISIBLE);
            holder.enterIcon.setVisibility(View.GONE);
        } else {
            holder.number.setVisibility(View.GONE);
            holder.enterIcon.setVisibility(View.VISIBLE);
        }
        if (item.type == 1) {
            holder.icon.setImageResource(R.drawable.message_icon_news);
            holder.title.setText("评论和赞");
            holder.line.setVisibility(View.VISIBLE);
        } else if (item.type == 2) {
            holder.icon.setImageResource(R.drawable.message_icon_follow);
            holder.title.setText("新粉丝");
            holder.line.setVisibility(View.VISIBLE);
        } else if (item.type == 3) {
            holder.icon.setImageResource(R.mipmap.message_icon_set);
            holder.title.setText("系统消息");
            holder.line.setVisibility(View.INVISIBLE);
        } else if (item.type == 4) {
            holder.icon.setImageResource(R.mipmap.message_icon_kefu);
            holder.title.setText("意见反馈");
        } else if (item.type == 5) {
            holder.icon.setImageResource(R.mipmap.message_icon_addresslist);
            holder.title.setText("小区通讯录");
        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_my_message_icon)
        RoundImageView icon;
        @BindView(R.id.m_list_item_my_message_title)
        TextView title;
        @BindView(R.id.m_list_item_my_message_enter_icon)
        ImageView enterIcon;
        @BindView(R.id.m_list_item_my_message_hot_tag)
        TextView number;
        @BindView(R.id.line)
        View line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}