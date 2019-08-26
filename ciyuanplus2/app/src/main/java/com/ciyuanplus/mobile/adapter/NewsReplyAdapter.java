package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailActivity;
import com.ciyuanplus.mobile.net.bean.ReplyItem;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 主页面新鲜事tab 里面的list adapter
 */

class NewsReplyAdapter extends BaseAdapter {
    //PostDetailActivity
    private final Activity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            final ReplyItem item = (ReplyItem) view.getTag();
            if (view.getId() == R.id.m_list_item_news_reply_lp) {
                if (Utils.isStringEquals(item.sendUserUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    ((ForumDetailActivity) mContext).tryDelete(item);
                } else {
                    if (KeyboardUtils.isSoftInputVisible(mContext)) {
                        KeyboardUtils.hideSoftInput(view);
                    } else {
                        ((ForumDetailActivity) mContext).replyReply(item);
                    }
                }
            }
        }
    };
    private ArrayList<ReplyItem> mList;
    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

            final ReplyItem item = (ReplyItem) view.getTag();
            if (view.getId() == R.id.m_list_item_news_reply_lp) {
                ((ForumDetailActivity) mContext).tryDelete(item);

            }
            return true;
        }
    };

    public NewsReplyAdapter(Activity mContext, ArrayList<ReplyItem> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    public void setmList(ArrayList<ReplyItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
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
        ReplyItem item = mList.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_news_reply, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ForegroundColorSpan span1 = new ForegroundColorSpan(0xff333333);
        ForegroundColorSpan span2 = new ForegroundColorSpan(0xff999999);
        String content = item.sendNickname + " 回复" + item.toNickname + " : " + item.contentText.replace("\r", "\n");
        SpannableString spanString = new SpannableString(content);
//        spanString.setSpan(span1, 0, item.sendNickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //粗斜体
        spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, item.sendNickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), (item.sendNickname + " 回复").length(), (item.sendNickname + " 回复" + item.toNickname ).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanString.setSpan(span2, item.sendNickname.length(), (item.sendNickname + " 回复" + item.toNickname).length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.content.setText(spanString);
//        holder.delete.setVisibility(Utils.isStringEquals(item.sendUserUuid, UserInfoData.getInstance().getUserInfoItem().uuid) ? View.VISIBLE : View.GONE);
//        holder.delete.setTag(item);
//        holder.delete.setOnClickListener(this);
        holder.layout.setTag(item);
        holder.layout.setOnClickListener(myOnClickListener);
//        holder.layout.setOnLongClickListener(mOnLongClickListener);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_news_reply_content)
        TextView content;
        @BindView(R.id.m_list_item_news_reply_lp)
        LinearLayout layout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
