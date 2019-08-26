package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.IViewHolder;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.module.forum_detail.rate_list.RateListActivity;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.DuplicateHeadIconImageView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 百科adapter
 * 限制电话号码最多只有三个， 这里不适用嵌套listview，否则效率会比较低
 */

public class WikiAdapter extends RecyclerView.Adapter<WikiAdapter.ViewHolder> {
    private final Activity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View v) {
            int id = v.getId();
            if (id == R.id.m_list_item_wiki_phone_text1) {
                String value = (String) v.getTag();
                callPhone(value);
            } else if (id == R.id.m_list_item_wiki_phone_text2) {
                String value = (String) v.getTag();
                callPhone(value);
            } else if (id == R.id.m_list_item_wiki_phone_text3) {
                String value = (String) v.getTag();
                callPhone(value);
            } else if (id == R.id.m_list_item_wiki_comment_lp) {
                String wikiId = (String) v.getTag();
                Intent intent = new Intent(mContext, RateListActivity.class);
                intent.putExtra(Constants.INTENT_WIKI_ID, wikiId);
                mContext.startActivity(intent);
            }
        }
    };
    private ArrayList<WikiItem> mWikiList;
    private final View.OnClickListener mItemClickListener;

    public WikiAdapter(Activity mContext, ArrayList<WikiItem> mWikiList, View.OnClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mWikiList = mWikiList;
        this.mItemClickListener = mItemClickListener;
    }

    public WikiItem getItem(int i) {
        return mWikiList.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wiki, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WikiItem item = mWikiList.get(position);
        if (!Utils.isStringEmpty(item.image)) {
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.image.split(",")[0]).apply(new RequestOptions().error(R.mipmap.imgfail))
                    .into(holder.type);
        }

        holder.title.setText(item.name);
        if (Utils.isStringEmpty(item.telephone)) {
            holder.phoneLayout.setVisibility(View.GONE);
        } else {
            holder.phoneLayout.setVisibility(View.VISIBLE);
            String[] phones = item.telephone.split(",");// 服务器给的数据是用"," 区分的。这里坐下处理
            if (phones.length > 2) {
                holder.phone3.setText(phones[2]);
                holder.phone3.setVisibility(View.VISIBLE);
                holder.phone3.setTag(phones[2]);
                holder.phone3.setOnClickListener(myOnClickListener);
            } else {
                holder.phone3.setVisibility(View.GONE);
            }
            if (phones.length > 1) {
                holder.phone2.setText(phones[1]);
                holder.phone2.setVisibility(View.VISIBLE);
                holder.phone2.setTag(phones[1]);
                holder.phone2.setOnClickListener(myOnClickListener);
            } else {
                holder.phone2.setVisibility(View.GONE);
            }
            holder.phone1.setText(phones[0]);
            holder.phone1.setTag(phones[0]);
            holder.phone1.setOnClickListener(myOnClickListener);
        }
        if (Utils.isStringEmpty(item.address)) {
            holder.addressLayout.setVisibility(View.GONE);
        } else {
            holder.addressLayout.setVisibility(View.VISIBLE);
            holder.address.setText(item.address);
        }
        if (item.userCount > 0) {
            holder.commentLayout.setTag(item.id);
            holder.commentLayout.setVisibility(View.VISIBLE);
            holder.commentLayout.setOnClickListener(myOnClickListener);
            holder.heads.setDataSource(item.userImgs);
            holder.comments.setText(item.userCount + "人评过 >");
        } else {
            holder.commentLayout.setVisibility(View.GONE);
        }

        if (!Utils.isStringEmpty(item.score)) {
            holder.numbers.setVisibility(View.VISIBLE);
            holder.numbersAlert.setVisibility(View.VISIBLE);
            holder.numbers.setText(item.score);
        } else {
            holder.numbers.setVisibility(View.GONE);
            holder.numbersAlert.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mWikiList == null ? 0 : mWikiList.size();
    }

    private void callPhone(final String number) {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage("" + number + "");
        builder.setPositiveButton("呼叫", (dialog, which) -> {
            // 1. 到了拨号界面，但是实际的拨号是由用户点击实现的。
            Intent intent = new Intent(Intent.ACTION_DIAL);
            // 2. 对用户没有提示，直接拨打电话
            // Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + number);
            intent.setData(data);
            mContext.startActivity(intent);
            dialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    class ViewHolder extends IViewHolder {
        @BindView(R.id.m_list_item_wiki_type_image)
        ImageView type;
        @BindView(R.id.m_list_item_wiki_title)
        TextView title;
        @BindView(R.id.m_list_item_wiki_phone_lp)
        LinearLayout phoneLayout;
        @BindView(R.id.m_list_item_wiki_location_lp)
        LinearLayout addressLayout;
        @BindView(R.id.m_list_item_wiki_phone_text1)
        TextView phone1;
        @BindView(R.id.m_list_item_wiki_phone_text2)
        TextView phone2;
        @BindView(R.id.m_list_item_wiki_phone_text3)
        TextView phone3;
        @BindView(R.id.m_list_item_wiki_location_text)
        TextView address;
        @BindView(R.id.m_list_item_wiki_heads)
        DuplicateHeadIconImageView heads;
        @BindView(R.id.m_list_item_wiki_comment_numbers)
        TextView comments;
        @BindView(R.id.m_list_item_wiki_numbers)
        TextView numbers;
        @BindView(R.id.m_list_item_wiki_numbers_alert)
        TextView numbersAlert;
        @BindView(R.id.m_list_item_wiki_comment_lp)
        RelativeLayout commentLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
