package com.dji.sdk.sample.demo.ILM;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.view.View;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.internal.utils.VideoFeedView;

import org.opencv.android.CameraActivity;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



import androidx.annotation.NonNull;
import dji.common.airlink.PhysicalSource;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.sdkmanager.DJISDKManager;

import static android.content.Context.MODE_PRIVATE;

public class ILMVideo extends CameraActivity implements TextureView.SurfaceTextureListener {

    private final VideoFeedView videoFeedView;
    private Mat grayFrame,rgbFrame,transpose_gray,transpose_rgb;
    private MatOfRect rects;
    private CascadeClassifier cascadeClassifier;
    private DJICodecManager djiCodecManager = null;
    VideoFeeder.VideoDataListener faceDectection;
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

        if(product.getCamera().isConnected()){
            // when the camera init()
            rgbFrame = new Mat();
            grayFrame =new Mat();

            rects = new MatOfRect();

            decFace();

            initSDKCallback();
        }

    }
    private void decFace() {

            faceDectection =new VideoFeeder.VideoDataListener() {
                @Override
                public void onReceive(byte[] bytes, int i) {
                    rgbFrame = new Mat(i , 1 , CvType.CV_8UC1);   // convert bytes to Mat
                    rgbFrame.put(0,0,bytes);                    // ...

                    transpose_gray = rgbFrame.t();

                    Imgproc.cvtColor(rgbFrame,grayFrame ,Imgproc.COLOR_RGB2GRAY);

                    transpose_rgb = grayFrame.t();

                    cascadeClassifier.detectMultiScale(transpose_gray,rects,1.1,2);

                    for(Rect rect : rects.toArray()){
                        // draw rects

                        Mat submat = transpose_gray.submat(rect);
                        Imgproc.blur(submat,submat,new Size(5,5));
                        Imgproc.rectangle(transpose_rgb,rect,new Scalar(0,255,0),5);

                        submat.release();

                    }
                    rgbFrame.get(0,0,bytes);

                    djiCodecManager.sendDataToDecoder(bytes, i);
                }
            };

        if(OpenCVLoader.initDebug()){
            videoFeedView.setEnabled(true);
            try {

                InputStream inputStream =getResources().openRawResource(R.raw.lbpcascade_frontalface);
                File file = new File (getDir("cascade",MODE_PRIVATE),"lbpcascade_frontalface.xml");
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                byte[] data =new byte[4096];
                int read_bytes=0;

                while ((read_bytes=inputStream.read(data))!=-1){
                    fileOutputStream.write(data,0,read_bytes);
                }

                cascadeClassifier = new CascadeClassifier(file.getAbsolutePath());

                if(cascadeClassifier.empty()){
                    cascadeClassifier = null;
                }

                inputStream.close();
                fileOutputStream.close();
                file.delete();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void initSDKCallback() {
        try {
            VideoFeeder.getInstance().getPrimaryVideoFeed().addVideoDataListener(faceDectection);
        } catch (Exception ignored) {
            throw new RuntimeException(ignored);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        if (djiCodecManager == null) {
            djiCodecManager = new DJICodecManager(this,surfaceTexture, i, i1);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        if (djiCodecManager != null) {
            djiCodecManager.cleanSurface();
            djiCodecManager = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }
}
