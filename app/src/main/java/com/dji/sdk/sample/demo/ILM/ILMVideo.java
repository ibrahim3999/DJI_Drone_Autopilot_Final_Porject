package com.dji.sdk.sample.demo.ILM;

import android.graphics.SurfaceTexture;
import android.view.View;

import com.dji.sdk.sample.internal.utils.VideoFeedView;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import dji.common.airlink.PhysicalSource;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.sdkmanager.LiveStreamManager;

public class ILMVideo {
    private final VideoFeedView videoFeedView;
    private DJICodecManager djiCodecManager;
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


            VideoFeeder.VideoDataListener primaryVideoDataListener = videoFeedView.registerLiveVideo(VideoFeeder.getInstance().getPrimaryVideoFeed(), true);

            djiCodecManager = new DJICodecManager(videoFeedView.getContext(), videoFeedView.getSurfaceTexture(), videoFeedView.getWidth(), videoFeedView.getHeight());
           // djiCodecManager = new DJICodecManager(videoFeedView.getContext(), videoFeedView.getSurfaceTexture(), 0, 0);



            VideoFeeder.getInstance().addPhysicalSourceListener(sourceListener);

            setupFaceDetection();

        }
    }

    private void setupFaceDetection() {

        CascadeClassifier faceCascade = new CascadeClassifier("app/src/main/res/raw/lbpcascade_frontalface.xml");

        VideoFeeder.VideoDataListener faceDetectionListener = new VideoFeeder.VideoDataListener() {
            @Override
            public void onReceive(final byte[] videoBuffer, final int size) {

                Mat mat = new Mat(size, 1, 1);
                mat.put(0, 0, videoBuffer);
                Mat grayMat = new Mat();
                Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_RGBA2GRAY);
                MatOfRect faces = new MatOfRect();
                faceCascade.detectMultiScale(grayMat, faces);
                Rect[] facesArray = faces.toArray();
                for (Rect rect : facesArray) {
                    Imgproc.rectangle(mat, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2);
                }

                mat.get(0, 0, videoBuffer);
                djiCodecManager.sendDataToDecoder(videoBuffer, size);


            }
        };

        VideoFeeder.getInstance().getPrimaryVideoFeed().addVideoDataListener(faceDetectionListener);
    }
}