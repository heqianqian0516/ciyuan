package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.comment_detail.CommentDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.bean.ReplyItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 评论详情页面里面的回复的 list adapter
 */

public class CommentDetailReplyAdapter extends BaseAdapter {
    private final Activity mContext;//PostDetailActivity
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if (view.getId() == R.id.m_list_item_comment_detail_reply_content) {
                final ReplyItem item = (ReplyItem) view.getTag();

                if (Utils.isStringEquals(item.sendUserUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    if (mContext instanceof PostDetailActivity)
                        ((PostDetailActivity) mContext).tryDelete(item);
                    else ((CommentDetailActivity) mContext).tryDelete(item);
                } else {
                    if (KeyboardUtils.isSoftInputVisible(mContext)) {
                        KeyboardUtils.hideSoftInput(view);
                    } else {
                        if (mContext instanceof PostDetailActivity)
                            ((PostDetailActivity) mContext).replyReply(item);
                        else ((CommentDetailActivity) mContext).replyReply(item);
                    }
                }
//                ((CommentDetailActivity) mContext).replyReply(item);
            } else if (view.getId() == R.id.m_list_item_comment_detail_reply_head) {
                final ReplyItem item = (ReplyItem) view.getTag(R.id.glide_item_tag);

                if (item.isAnonymous == 1 || Utils.isStringEquals(item.sendUserUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(mContext, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.sendUserUuid);
                mContext.startActivity(intent);
            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

            if (view.getId() == R.id.m_list_item_comment_detail_reply_content) {
                final ReplyItem item = (ReplyItem) view.getTag();

                if (Utils.isStringEquals(item.sendUserUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    if (mContext instanceof PostDetailActivity)
                        ((PostDetailActivity) mContext).tryDelete(item);
                    else ((CommentDetailActivity) mContext).tryDelete(item);
                }

            }
            return false;
        }
    };
    private ArrayList<ReplyItem> mList;

    public CommentDetailReplyAdapter(Activity mContext, ArrayList<ReplyItem> list) {
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_comment_detail_news_reply, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(item.sendNickname);
        Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.sendPhoto).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop()).into(holder.head);

        ForegroundColorSpan span1 = new ForegroundColorSpan(0xff333333);
        SpannableString spanString = new SpannableString("回复" + item.toNickname + " : " + item.contentText.replace("\r", "\n"));
        spanString.setSpan(new StyleSpan(Typeface.BOLD), ("回复").length(), (" 回复" + item.toNickname).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanString.setSpan(span1, 0, ("回复" + item.toNickname).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.content.setText(spanString);

        holder.content.setTag(item);
        holder.content.setOnClickListener(myOnClickListener);
        holder.head.setTag(R.id.glide_item_tag, item);
        holder.head.setOnClickListener(myOnClickListener);

        holder.content.setTag(item);
//        holder.content.setOnLongClickListener(mOnLongClickListener);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_comment_detail_reply_content)
        TextView content;
        @BindView(R.id.m_list_item_comment_detail_reply_name)
        TextView name;
        @BindView(R.id.m_list_item_comment_detail_reply_head)
        RoundImageView head;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
