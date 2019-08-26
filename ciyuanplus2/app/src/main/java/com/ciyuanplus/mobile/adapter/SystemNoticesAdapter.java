package com.ciyuanplus.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.chat.SystemMessageListActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.net.bean.NoticeItem;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 */

public class SystemNoticesAdapter extends BaseAdapter {
    private final SystemMessageListActivity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            int id = view.getId();
            NoticeItem item = (NoticeItem) view.getTag();
            if (id == R.id.m_list_item_system_notice_delete) {
                mContext.deleteNotice(item);
            }
        }
    };
    private final ArrayList<NoticeItem> mList;

    public SystemNoticesAdapter(SystemMessageListActivity mContext, ArrayList<NoticeItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_system_notice, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.detail.setText(item.contentText);
        holder.detail.setTextColor(item.isRead == 1 ? 0xff999999 : 0xff333333);
//        holder.newIcon.setVisibility(item.isRead == 1 ? View.GONE : View.VISIBLE);
        holder.time.setText(Utils.getFormattedTimeString(item.createTime));
        holder.delete.setTag(item);
        holder.delete.setOnClickListener(myOnClickListener);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_system_notice_detail)
        TextView detail;
        @BindView(R.id.m_list_item_system_notice_time)
        TextView time;
        @BindView(R.id.m_list_item_system_notice_delete)
        ImageView delete;
        @BindView(R.id.m_list_item_system_notice_new_tag)
        ImageView newIcon;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}