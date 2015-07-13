package com.entropicapps.deeplistener;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    public MainService() {
    }

    final SoundMeter recorder = new SoundMeter();
    final Handler mHandler = new Handler();
    double FIRST_VALUE = 0;
    double NEXT_VALUE = 0;
    double DIFFERENCE = 0;
    int threshold;
    int interval;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        threshold = intent.getIntExtra("threshold", 30000);
        interval = intent.getIntExtra("interval", 1000);
        recorder.start();

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTV();
            }
        }, 0, 1000);

        return START_STICKY;
    }

    private void updateTV() {
        //Update amplitude here
        FIRST_VALUE = recorder.getAmplitude();
        mHandler.post(mRunnable);
    }

    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            DIFFERENCE = FIRST_VALUE - NEXT_VALUE;
            NEXT_VALUE = FIRST_VALUE;

            if (DIFFERENCE >= threshold) {
                Toast.makeText(getApplicationContext(), "Threshold exceeded", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        recorder.stop();
    }
}
