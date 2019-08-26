package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.HelpItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 主页面新鲜事tab 里面的list adapter
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private final ArrayList<HelpItem> mHelpList;
    private final View.OnClickListener mItemClickListener;

    public QuestionAdapter(Context mContext, ArrayList<HelpItem> mHelpList, View.OnClickListener mItemClickListener) {
        Context context = mContext;
        this.mHelpList = mHelpList;
        this.mItemClickListener = mItemClickListener;
    }

    public HelpItem getItem(int i) {
        return mHelpList.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_question, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HelpItem item = mHelpList.get(position);
        holder.answer.setText(item.answer);
        holder.question.setText(item.question);
        if (item.isSpeard) {
            holder.opera.setImageResource(R.mipmap.launch_icon_open);
            holder.answerLayout.setVisibility(View.VISIBLE);
        } else {
            holder.opera.setImageResource(R.mipmap.launch_icon_stop);
            holder.answerLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mHelpList == null ? 0 : mHelpList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_question_title)
        TextView question;
        @BindView(R.id.m_list_item_question_answer)
        TextView answer;
        @BindView(R.id.m_help_question_spread)
        ImageView opera;
        @BindView(R.id.m_help_answer_lp)
        RelativeLayout answerLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
