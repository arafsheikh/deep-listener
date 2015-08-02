package com.entropicapps.deeplistener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.KeyEvent;
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
    AudioManager am;
    AudioManager.OnAudioFocusChangeListener af;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        af = new AudioManager.OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                    // Lower the volume
                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    // Raise it back to normal
                }
            }
        };

        threshold = intent.getIntExtra("threshold", 30000);
        interval = intent.getIntExtra("interval", 1000);
        recorder.start();

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateAmp();
            }
        }, 0, interval);

        return START_STICKY;
    }

    private void updateAmp() {
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
                //Toast.makeText(getApplicationContext(), "Threshold exceeded", Toast.LENGTH_SHORT).show();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);

                am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                int request = am.requestAudioFocus(af,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);

            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        recorder.stop();
    }
}
