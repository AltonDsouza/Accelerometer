package com.example.alton.accelerometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;

import java.util.Timer;


public class MainActivity extends AppCompatActivity  {
    TextView t1, t2, t3;
    Timer timer;
    MyBroadcastReceiver broadcastReceiver;

    // private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = (TextView) findViewById(R.id.xVal);
        t2 = (TextView) findViewById(R.id.yVal);
        t3 = (TextView) findViewById(R.id.zVal);
        timer = new Timer();

        // Log.d(TAG,"registered listener successfully");
        startService(new Intent(this,TimeService.class));
        //t1.setText(String.valueOf(deltaX1));
        //t2.setText(String.valueOf(deltaY1));
        //t3.setText(String.valueOf(deltaZ1));

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this,TimeService.class));
    }

    public void onResume() {
        super.onResume();
        broadcastReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter("YourAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    public void onPause() {
        super.onPause();
        if (broadcastReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           // And if you added extras to the intent get them here too
            // this needs some null checks
            Bundle b = intent.getExtras();
            String yourValue = b.getString("xval");
            t1.setText(yourValue);
            String someDouble = b.getString("yval");
            ///do something with someDouble
            t2.setText(someDouble);
            String your = b.getString("zval");
            t3.setText(your);

        }
    }
}