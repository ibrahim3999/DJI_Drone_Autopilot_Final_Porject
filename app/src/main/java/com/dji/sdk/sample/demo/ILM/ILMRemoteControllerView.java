package com.dji.sdk.sample.demo.ILM;

import android.content.Context;

import com.dji.sdk.sample.R;

import org.osmdroid.views.MapView;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.controller.MainActivity;
import com.dji.sdk.sample.internal.utils.ModuleVerificationUtil;
import com.dji.sdk.sample.internal.utils.VideoFeedView;
import com.dji.sdk.sample.internal.view.PresentableView;

import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.flightcontroller.FlightController;

public class ILMRemoteControllerView extends RelativeLayout implements View.OnClickListener, PresentableView {
    private MapView mapView;
    private Context context;
    private Button goTobtn;
    private Button stopbtn;
    private Button landbtn;
    private SurfaceView surfaceView;
    private VideoFeedView videoFeedView;
    private View coverView;
    private VideoFeeder.PhysicalSourceListener sourceListener;
    protected DJICodecManager codecManager = null;
    private ILMMapController mapController;
    private ILMVideoController videoController;
    private ILMStatusBar statusBar;
    private ILMCSVLog csvLog;

    public ILMRemoteControllerView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        setClickable(true);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_ilm_remote_controller, this, true);
        initUI();
    }

    private void initUI() {
        stopbtn = findViewById(R.id.btn_ILM_Stop);
        landbtn = findViewById(R.id.btn_ILM_Land);
        goTobtn = findViewById(R.id.btn_ILM_GoTo);
        //<<====================Status Bar====================>>//
        statusBar = new ILMStatusBar(context);
        addView(statusBar);
        //<<====================CSV Log====================>>//
        csvLog = new ILMCSVLog(context, statusBar);
        csvLog.createLogBrain();
        //<<====================Map====================>>//
        mapView = findViewById(R.id.mapView_ILM);
        mapController = new ILMMapController(context, mapView);
        //<<====================Video====================>>//
        videoFeedView = findViewById(R.id.videoFeedView_ILM);
        coverView = findViewById(R.id.view_ILM_coverView);
        videoController = new ILMVideoController(videoFeedView, coverView);
        videoController.displayVideo();
        //<<====================Status Bar Updates====================>>//
        statusBar.updateDateTime();
        statusBar.updateBattery();
        statusBar.updateSpeed();
        statusBar.updateXYZ();
        statusBar.updateLatitudeLongitude();
        statusBar.updatePitchRollYaw();
        //<<====================Buttons====================>>//
        stopbtn.setOnClickListener(this);
        landbtn.setOnClickListener(this);
        goTobtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FlightController flightController = ModuleVerificationUtil.getFlightController();
        if(flightController == null ){
            // Handle the case when FlightController is not available
            return;
        }
        switch (v.getId()) {
            case R.id.btn_ILM_Stop:
                break;
            case R.id.btn_ILM_Land:
                break;
            case R.id.btn_ILM_GoTo:
                break;
            default:
                break;
        }
    }

    @Override
    public int getDescription() {
        return R.string.component_listview_ilm_remote_controller;
    }

    @NonNull
    @Override
    public String getHint() {
        return this.getClass().getSimpleName() + ".java";
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        DJISampleApplication.getEventBus().post(new MainActivity.RequestStartFullScreenEvent());
    }

    @Override
    protected void onDetachedFromWindow() {
        csvLog.closeLogBrain();     //Closing CSV
        DJISampleApplication.getEventBus().post(new MainActivity.RequestEndFullScreenEvent());
        super.onDetachedFromWindow();
    }

}

