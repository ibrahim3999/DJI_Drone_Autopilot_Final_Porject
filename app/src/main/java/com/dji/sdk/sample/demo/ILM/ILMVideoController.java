package com.dji.sdk.sample.demo.ILM;

import android.view.View;
import android.widget.TextView;

import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.internal.utils.VideoFeedView;

import dji.common.airlink.PhysicalSource;
import dji.common.error.DJIError;
import dji.keysdk.callback.SetCallback;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.sdkmanager.DJISDKManager;

public class ILMVideoController {
    private final VideoFeedView videoFeedView;
    private VideoFeeder.PhysicalSourceListener sourceListener;

    public ILMVideoController(VideoFeedView videoFeedView, View coverView) {
        this.videoFeedView = videoFeedView;
        this.videoFeedView.setCoverView(coverView);
        initCallbacks();
    }

    private void initCallbacks() {
        SetCallback setBandwidthCallback = new SetCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.setResultToToast("Set key value successfully");
                if (videoFeedView != null) {
                    videoFeedView.changeSourceResetKeyFrame();
                }
            }

            @Override
            public void onFailure(DJIError error) {
                ToastUtils.setResultToToast("Failed to set: " + error.getDescription());
            }
        };
    }

    public void displayVideo() {
        sourceListener = new VideoFeeder.PhysicalSourceListener() {
            @Override
            public void onChange(VideoFeeder.VideoFeed videoFeed, PhysicalSource newPhysicalSource) {
                if (videoFeed == VideoFeeder.getInstance().getPrimaryVideoFeed()) {
                    String newText = "Primary Source: " + newPhysicalSource.toString();
                    TextView primaryVideoFeedTitle = null;
                    ToastUtils.setResultToText(primaryVideoFeedTitle, newText);
                }
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
