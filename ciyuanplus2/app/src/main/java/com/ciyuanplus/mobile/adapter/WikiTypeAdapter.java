package com.ciyuanplus.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.wiki.around_wiki.AroundWikiActivity;
import com.ciyuanplus.mobile.net.bean.WikiTypeItem;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * Wiki 类型  列表适配器
 */

public class WikiTypeAdapter extends RecyclerView.Adapter<WikiTypeAdapter.ViewHolder> {
    private final AroundWikiActivity mContext;
    private final WikiTypeItem[] wikiTypeItems;
    private View.OnClickListener mItemClickListener;

    public WikiTypeAdapter(AroundWikiActivity mContext, WikiTypeItem[] wikiTypeItems, View.OnClickListener itemClickListener) {
        this.mContext = mContext;
        this.wikiTypeItems = wikiTypeItems;
        this.mItemClickListener = itemClickListener;

    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public WikiTypeItem getItem(int i) {
        return wikiTypeItems[i];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wiki_type, parent, false);
        WikiTypeAdapter.ViewHolder holder = new WikiTypeAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WikiTypeItem item = wikiTypeItems[position];
        if (mContext.getWikiType() != null && Utils.isStringEquals(mContext.getWikiType().id, item.id)) {
            holder.selected.setSelected(true);
        } else {
            holder.selected.setSelected(false);
        }
        holder.name.setText(item.name);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return wikiTypeItems == null ? 0 : wikiTypeItems.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_wiki_type_name)
        TextView name;
        @BindView(R.id.m_list_item_wiki_type_selected)
        LinearLayout selected;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }
    }
}
