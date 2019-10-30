package com.ciyuanplus.mobile.module.video;


import java.util.HashMap;

public class VideoPresenter extends IVideoContract.VideoPresenter {
    private IVideoContract.IVideoView mView;
    private VideoModel videoModel;

    public VideoPresenter(IVideoContract.IVideoView mView) {
        this.mView = mView;
        videoModel = new VideoModel();
    }
    @Override
    public void recommendVideo(HashMap<String, String> params) {
        if (videoModel!=null){
            videoModel.getRecommendVideo(params, new IVideoCallback() {
                @Override
                public void success(String result) {
                    if (mView!=null){
                        mView.success(result);
                    }
                }

                @Override
                public void error(String msg) {
                    if (mView!=null){
                        mView.failure(msg);
                    }
                }
            });
        }
    }

    @Override
    public void attentionVideo(String params) {

    }
}
