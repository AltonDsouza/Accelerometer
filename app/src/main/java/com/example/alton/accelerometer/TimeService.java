package com.example.alton.accelerometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by alton on 9/22/2018.
 */

import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor acceloremeter;
    private float deltaX1 = 0;
    private float deltaY1 = 0;
    private float deltaZ1 = 0;
    Context ctx;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();

    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acceloremeter=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,acceloremeter,SensorManager.SENSOR_DELAY_NORMAL);
        if (mTimer != null) {
            mTimer.cancel();
            sensorManager.unregisterListener(this);
        } else {


            mTimer = new Timer();

        }

        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, 3000);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        deltaX1 = (float) ((sensorEvent.values[0]));
        deltaY1 = (float) ((sensorEvent.values[1]));
        deltaZ1 = (float) ((sensorEvent.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {

            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    //  Toast.makeText(getApplicationContext(),"xval"+deltaX1+" yval:"+deltaY1+" zval:"
                                           //    +deltaZ1,Toast.LENGTH_LONG).show();
                    //passing values with send broadcast
                   Intent intent = new Intent("YourAction");
                    Bundle bundle = new Bundle();
                    bundle.putString("xval",Float.toString(deltaX1));
                    bundle.putString("yval",Float.toString(deltaY1));
                    bundle.putString("zval",Float.toString(deltaZ1));
                    intent.putExtras(bundle);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


                }

            });
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        sensorManager.unregisterListener(this);
    }
}

