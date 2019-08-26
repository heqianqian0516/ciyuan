package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.PostTypeItem;
import com.ciyuanplus.mobile.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 发布长文里面的 标签选择
 */

public class PopupTagAdapter extends BaseAdapter {
    private final Activity mContext;
    private final PostTypeItem[] postTypeItems;
    private PostTypeItem mDefalutType;

    public PopupTagAdapter(Activity mContext, PostTypeItem[] postTypeItems) {
        this.mContext = mContext;
        this.postTypeItems = postTypeItems;
        this.mDefalutType = postTypeItems[0];
    }

    public void setmDefalutType(PostTypeItem mDefalutType) {
        this.mDefalutType = mDefalutType;
    }

    @Override
    public int getCount() {
        return postTypeItems == null ? 0 : postTypeItems.length;
    }

    @Override
    public PostTypeItem getItem(int i) {
        return postTypeItems[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        PostTypeItem item = postTypeItems[i];
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_popup_tag, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDefalutType != null && Utils.isStringEquals(mDefalutType.typeId, item.typeId)) {
            holder.name.setTextColor(0xfff16520);
        } else {
            holder.name.setTextColor(0xff666666);
        }
        holder.name.setText(item.typeName);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_popup_tag_name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
