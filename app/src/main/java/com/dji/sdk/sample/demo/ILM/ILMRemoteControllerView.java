package com.dji.sdk.sample.demo.ILM;

import android.content.Context;

import com.dji.sdk.sample.R;

import org.osmdroid.views.MapView;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.controller.MainActivity;

import com.dji.sdk.sample.internal.utils.VideoFeedView;
import com.dji.sdk.sample.internal.view.PresentableView;

public class ILMRemoteControllerView extends RelativeLayout implements View.OnClickListener, PresentableView {
    private MapView mapView;
    private Context context;
    private VideoFeedView videoFeedView;
    private View coverView;
    private ILMMap mapController;
    private ILMVideo videoController;
    private ILMStatusBar statusBar;
    private ILMCSVLog csvLog;
    private ILMButtons buttons;

    public ILMRemoteControllerView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        setClickable(true);
        //<<=====================Status Bar View==========================>>//
        statusBar = new ILMStatusBar(context);
        addView(statusBar);
        //<<==============================================================>>//
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_ilm_remote_controller, this, true);
        initUI();
    }

    private void initUI() {
        //<<==========================CSV Log==========================>>//
        csvLog = new ILMCSVLog(context, statusBar);
        csvLog.createLogBrain();
        //<<==========================Map==========================>>//
        mapView = findViewById(R.id.mapView_ILM);
        mapController = new ILMMap(context, mapView);
        mapController.initMap();
        //<<==========================Video==========================>>//
        videoFeedView = findViewById(R.id.videoFeedView_ILM);
        coverView = findViewById(R.id.view_ILM_coverView);
        videoController = new ILMVideo(videoFeedView, coverView);
        videoController.displayVideo();
        //<<==========================Status Bar Updates==========================>>//
        statusBar.updateDateTime();
        statusBar.updateBattery();
        statusBar.updateSpeed();
        statusBar.updateXYZ();
        statusBar.updateLatitudeLongitude();
        statusBar.updatePitchRollYaw();
        //<<==========================Buttons==========================>>//
        buttons = new ILMButtons(context, this);
        buttons.takeOffbtn.setOnClickListener(this);
        buttons.stopbtn.setOnClickListener(this);
        buttons.landbtn.setOnClickListener(this);
        buttons.goTobtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ILM_Take_Off:
                buttons.takeOff();
                break;
            case R.id.btn_ILM_Stop:
                buttons.stop();
                break;
            case R.id.btn_ILM_Land:
                buttons.land();
                break;
            case R.id.btn_ILM_GoTo:
                buttons.goTo();
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

