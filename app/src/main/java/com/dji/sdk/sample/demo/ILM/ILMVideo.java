package com.dji.sdk.sample.demo.ILM;

import android.view.View;

import com.dji.sdk.sample.internal.utils.VideoFeedView;

import dji.common.airlink.PhysicalSource;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.sdkmanager.DJISDKManager;

public class ILMVideo {
    private final VideoFeedView videoFeedView;

    public ILMVideo(VideoFeedView videoFeedView, View coverView) {
        this.videoFeedView = videoFeedView;
        this.videoFeedView.setCoverView(coverView);
    }

    public void displayVideo() {
        VideoFeeder.PhysicalSourceListener sourceListener = new VideoFeeder.PhysicalSourceListener() {
            @Override
            public void onChange(VideoFeeder.VideoFeed videoFeed, PhysicalSource newPhysicalSource) {
            }
        };
        if (VideoFeeder.getInstance() == null) return;
        final BaseProduct product = DJISDKManager.getInstance().getProduct();
        if (product != null) {
            VideoFeeder.VideoDataListener primaryVideoDataListener =
                    videoFeedView.registerLiveVideo(VideoFeeder.getInstance().getPrimaryVideoFeed(), true);
            VideoFeeder.getInstance().addPhysicalSourceListener(sourceListener);
        }
    }

}
