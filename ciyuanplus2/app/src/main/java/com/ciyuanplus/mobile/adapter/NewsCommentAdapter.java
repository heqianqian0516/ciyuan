package com.ciyuanplus.mobile.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.comment_detail.CommentDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.bean.CommentItem;
import com.ciyuanplus.mobile.net.bean.ReplyItem;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.ListViewForScrollView;
import com.ciyuanplus.mobile.widget.MarkView;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Alen
 * @date 2017/5/11
 * 主页面新鲜事tab 里面的list adapter
 */
public class NewsCommentAdapter extends BaseAdapter {
    private final ForumDetailActivity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if (view.getId() == R.id.m_list_item_news_comment_head_image) {
                final CommentItem item = (CommentItem) view.getTag(R.id.glide_item_tag);
                if (item.isAnonymous == 1 || Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    return;
                }
                Intent intent = new Intent(mContext, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.userUuid);
                mContext.startActivity(intent);
            } else if (view.getId() == R.id.m_list_item_news_comment_lp) {
                final CommentItem item = (CommentItem) view.getTag();
                if (Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    // 点击自己的帖子 删除
                    mContext.tryDelete(item);
                } else {
                    if (KeyboardUtils.isSoftInputVisible(mContext)) {
                        KeyboardUtils.hideSoftInput(view);
                    } else {
                        mContext.repleyComment(item);
                    }
                }
            } else if (R.id.m_list_item_news_comment_more == view.getId()) {
                final CommentItem item = (CommentItem) view.getTag();
                String uuid;
                uuid = mContext.mPresenter.mUuid;
                Intent intent = new Intent(mContext, CommentDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, uuid);
                intent.putExtra(Constants.INTENT_COMMENT_ID_ITEM, item.commentUuid);
                mContext.startActivity(intent);
            } else if (view.getId() == R.id.m_list_item_news_comment_like_image) {
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                CommentItem item = (CommentItem) view.getTag();
                if (item.isLike == 1) {
                    mContext.mPresenter.cancelRequestLikeComment(item.commentUuid);
                } else {
                    mContext.mPresenter.requestLikeComment(item.commentUuid);
                }
            } else if (view.getId() == R.id.iv_accept
                    || view.getId() == R.id.tv_accept
                    || view.getId() == R.id.rl_accept_layout) {
                // 求助 采纳
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }

                CommentItem item = (CommentItem) view.getTag();

                showAcceptDialog(item);
            }
        }
    };
    private ArrayList<CommentItem> mList;
    private final int mBizType;

    public NewsCommentAdapter(ForumDetailActivity mContext, ArrayList<CommentItem> list, int mBizType) {
        this.mContext = mContext;
        this.mList = list;
        this.mBizType = mBizType;
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
        CommentItem item = mList.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_news_comment, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(item.nickname);

        RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_head_).error(R.mipmap.default_head_).dontAnimate().centerCrop();

        Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(options).into(holder.head);

        if (Utils.isStringEmpty(item.contentText)) {
            holder.content.setVisibility(View.GONE);
        } else {
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(item.contentText.replace("\r", "\n"));
        }
        if (item.replyCommentResults != null && item.replyCommentResults.length > 0) {
            holder.childLayout.setVisibility(View.VISIBLE);

            ArrayList<ReplyItem> templist = new ArrayList<>();
            for (int k = 0; k < item.replyCommentResults.length && k < 4; k++) {
                templist.add(item.replyCommentResults[k]);
            }
            holder.comment.setAdapter(new NewsReplyAdapter(mContext, templist));

        } else {
            holder.childLayout.setVisibility(View.GONE);
        }
        holder.time.setText(Utils.getFormattedTimeString(item.createTime));

        if (item.replyCommentResults != null && item.replyCommentResults.length > 4) {
            holder.more.setVisibility(View.VISIBLE);
            holder.more.setTag(item);
            holder.more.setOnClickListener(myOnClickListener);
        } else {
            holder.more.setVisibility(View.GONE);
        }

        holder.sex.setImageResource(UserInfoItem.getSexImageResource(item.sex));
        if (item.isAnonymous == 1) {
            holder.sex.setVisibility(View.GONE);
            holder.head.setOnClickListener(null);
        } else {
            holder.sex.setVisibility(View.VISIBLE);
            holder.head.setTag(R.id.glide_item_tag, item);
            holder.head.setOnClickListener(myOnClickListener);
        }
        holder.likeNumber.setText(String.format(Locale.getDefault(), "%d", item.likeCount));
        holder.likeImage.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
        holder.likeImage.setImageResource(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
        if (item.score > 0) {
            holder.markView.setVisibility(View.VISIBLE);
            holder.markView.setValue(item.score);
        } else {
            holder.markView.setVisibility(View.GONE);
        }

        holder.likeImage.setTag(item);
        holder.likeImage.setOnClickListener(myOnClickListener);
        holder.layout.setTag(item);
        holder.layout.setOnClickListener(myOnClickListener);
        holder.acceptLayout.setTag(item);
        holder.acceptLayout.setOnClickListener(myOnClickListener);
        return convertView;
    }

    //采纳对话框
    private void showAcceptDialog(CommentItem item) {

        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage("确定采纳这个回答吗？");
        builder.setPositiveButton("确定", (dialog, which) -> {

            dialog.dismiss();
            mContext.mPresenter.acceptAnswer(item.commentUuid);

        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    class ViewHolder {
        @BindView(R.id.m_list_item_news_comment_lp)
        LinearLayout layout;
        @BindView(R.id.m_list_item_news_comment_time_text)
        TextView time;
        @BindView(R.id.m_list_item_news_comment_name_text)
        TextView name;
        @BindView(R.id.m_list_item_news_comment_head_image)
        RoundImageView head;
        @BindView(R.id.m_list_item_news_comment_content_text)
        TextView content;
        @BindView(R.id.m_list_item_news_comment_more)
        TextView more;
        @BindView(R.id.m_list_item_news_comment_child)
        ListViewForScrollView comment;
        @BindView(R.id.m_list_item_news_comment_child_lp)
        RelativeLayout childLayout;
        @BindView(R.id.m_list_item_news_comment_head_sex)
        ImageView sex;
        @BindView(R.id.m_list_item_news_comment_is_neighbor)
        ImageView neighbor;
        @BindView(R.id.m_list_item_news_comment_like_number)
        TextView likeNumber;
        @BindView(R.id.m_list_item_news_comment_like_image)
        ImageView likeImage;
        @BindView(R.id.m_list_item_news_comment_mark_view)
        MarkView markView;
        @BindView(R.id.iv_accept)
        ImageView acceptImage;
        @BindView(R.id.tv_accept)
        TextView acceptText;
        @BindView(R.id.rl_accept_layout)
        View acceptLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
