/*
package com.dji.sdk.sample.demo.ILM;

import android.util.Log;
import android.view.View;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.internal.utils.VideoFeedView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.InputStream;

import dji.sdk.base.BaseProduct;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.sdkmanager.DJISDKManager;

public class ILMVideo_Object_Detection  extends ILMVideo implements CameraBridgeViewBase.CvCameraViewListener{

    private CascadeClassifier mjavaDetector;
    private MatOfRect mObjectRectangles;
    private CameraBridgeViewBase cameraBridgeViewBase;
    private VideoFeedView videoFeedView;
    public ILMVideo_Object_Detection(VideoFeedView videoFeedView, View coverView) {
        super(videoFeedView, coverView);
        this.videoFeedView = videoFeedView;

        mjavaDetector = new CascadeClassifier("app/src/main/res/raw/lbpcascade_frontalface.xml");

        if(mjavaDetector.empty()){
            ToastUtils.setResultToToast("Error loading cascade classifier");
            return;
        }
        mObjectRectangles = new MatOfRect();
    }

    @Override
    public void displayVideo() {
        super.displayVideo();

    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        Mat grayFrame= new Mat();
        Imgproc.cvtColor(inputFrame,grayFrame,Imgproc.COLOR_RGBA2GRAY);

        //decet face detection
        mjavaDetector.detectMultiScale(grayFrame,mObjectRectangles,1.1,2,2,new Size(30,30),new Size());
        Rect[] objectRectArray = mObjectRectangles.toArray();

        //draw rect around decet
        for (Rect rect : objectRectArray) {
            Imgproc.rectangle(inputFrame, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2);
        }

        grayFrame.release();
        inputFrame.release();
        return inputFrame;
    }

}


*/

