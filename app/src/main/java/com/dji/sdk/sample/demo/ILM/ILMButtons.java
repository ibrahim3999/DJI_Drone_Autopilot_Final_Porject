package com.dji.sdk.sample.demo.ILM;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.R;

import com.dji.sdk.sample.internal.utils.DialogUtils;
import com.dji.sdk.sample.internal.utils.ModuleVerificationUtil;

import dji.common.error.DJIError;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.common.util.CommonCallbacks.CompletionCallback;
import dji.sdk.flightcontroller.FlightController;
import dji.common.mission.waypoint.Waypoint;


public class ILMButtons {
    protected Button goTobtn;
    protected Button stopbtn;
    protected Button landbtn;
    protected Button takeOffbtn;
    private Context context;
    FlightController flightController = ModuleVerificationUtil.getFlightController();
    private Waypoint waypoint = new Waypoint();
    private CompletionCallback callback = new CompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
            if (djiError == null) {
                DialogUtils.showDialog(context, context.getResources().getString(R.string.success));
            } else {
                DialogUtils.showDialog(context, djiError.getDescription());
            }
        }
    };

    public ILMButtons(Context context, View view) {
        goTobtn = view.findViewById(R.id.btn_ILM_GoTo);
        stopbtn = view.findViewById(R.id.btn_ILM_Stop);
        landbtn = view.findViewById(R.id.btn_ILM_Land);
        takeOffbtn = view.findViewById(R.id.btn_ILM_Take_Off);
        this.context = context;
    }

    protected void takeOff() {
        flightController.startTakeoff(callback);
    }

    protected void stop() {
        flightController.cancelGoHome(callback);
        flightController.cancelTakeoff(callback);
        flightController.cancelLanding(callback);
    }

    protected void land() {
        flightController.startLanding(callback);
    }

    protected void goTo() {
        waypoint.coordinate = new LocationCoordinate2D(32.101355, 35.202021);
    }
}

