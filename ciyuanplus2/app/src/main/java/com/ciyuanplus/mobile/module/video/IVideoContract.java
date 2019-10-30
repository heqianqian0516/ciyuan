package com.ciyuanplus.mobile.module.video;


import java.util.HashMap;

public interface IVideoContract {
    abstract class VideoPresenter {
        public abstract void recommendVideo( HashMap<String, String> params);
        public abstract void attentionVideo(String params);
    }

    interface IVideoView {
        void success(String result);
        void failure(String msg);

    }



    interface IVideoModel {
        void getRecommendVideo(HashMap<String, String> params, IVideoCallback callback);
    }
}
