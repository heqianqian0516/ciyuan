package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.CommunityItem;
import com.ciyuanplus.mobile.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 个人中心编辑页面里面的  小区选择弹出框 adapter
 */

public class PopupLocationAdapter extends BaseAdapter {
    private final CommunityItem mDefaultCommunity = new CommunityItem();
    private final Context mContext;
    private CommunityItem[] communityItems;

    public PopupLocationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setCommunityItems(CommunityItem[] communityItems) {
        this.communityItems = communityItems;
    }

    public void setmDefaultCommunity(String uuid, String name) {
        this.mDefaultCommunity.uuid = uuid;
        this.mDefaultCommunity.commName = name;
    }

    @Override
    public int getCount() {
        return communityItems == null ? 0 : communityItems.length;
    }

    @Override
    public CommunityItem getItem(int i) {
        return communityItems[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        CommunityItem item = communityItems[i];
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_popup_location, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (Utils.isStringEquals(mDefaultCommunity.uuid, item.uuid)) {
            holder.name.setTextColor(0xff43a6df);
        } else {
            holder.name.setTextColor(0xff666666);
        }
        holder.name.setText(item.commName);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_popup_location_name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
