package com.ciyuanplus.mobile.module.forum_detail.forum_detail;

import android.content.Intent;
import android.widget.ListView;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

public class ForumDetailContract {
    public interface Presenter extends BaseContract.Presenter{
        void initData(Intent intent);

        void resetCommentData();

        void likePost();

        void cancelLikePost();

        void submitCommentOrRely(String comment);

        void requestPostComments(boolean b);

        void requestForumDetail();
    }

    public interface View extends BaseContract.View{
        ListView getDetailCommentListView();

        void stopRefreshAndLoadMore();

        void updateCommentView(int size);

        void updateView();

        void setCommentText(String s);

        void setCommentInput(String s);

        void updateBottomView();

        void finishRefresh();

        void finishLoadMore();

        void setLoadMoreEnable(boolean enable);
    }
}
