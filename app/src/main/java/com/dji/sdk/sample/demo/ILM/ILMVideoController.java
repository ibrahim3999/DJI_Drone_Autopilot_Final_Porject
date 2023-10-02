package com.dji.sdk.sample.demo.ILM;

import android.view.View;

import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.internal.utils.VideoFeedView;

import dji.common.error.DJIError;
import dji.keysdk.callback.SetCallback;

public class ILMVideoController {
    private final VideoFeedView videoFeedView;

    public ILMVideoController(VideoFeedView videoFeedView, View coverView) {
        this.videoFeedView = videoFeedView;
        videoFeedView.setCoverView(coverView);
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
}
