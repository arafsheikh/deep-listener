package com.entropicapps.deeplistener;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    double ampValue;
    //TextView tvDebug;
    final Handler mHandler = new Handler();
    final SoundMeter recorder = new SoundMeter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final TextView tvDebug = (TextView) findViewById(R.id.tvDebug);
        final Button btStart = (Button) findViewById(R.id.btStart);
        final Button btStop = (Button) findViewById(R.id.btStop);
        btStop.setEnabled(false);


        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.start();
                btStop.setEnabled(true);
                btStart.setEnabled(false);

                /*
                double amp = -1;
                amp = recorder.getAmplitude();
                tvDebug.setText(String.valueOf(amp));
                // Execute code after 0.5 seconds have passed
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        double newAmp = -2;
                        newAmp = recorder.getAmplitude();
                        tvDebug.setText(String.valueOf(newAmp));
                    }
                }, 500);
                */

                Timer mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        updateTV();
                    }
                }, 0, 1000);

            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stop();
                btStop.setEnabled(false);
                btStart.setEnabled(true);
            }
        });

    }

    private void updateTV() {
        //Update amplitude here
        ampValue = recorder.getAmplitude();
        mHandler.post(mRunnable);
    }

    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            TextView tvDebug = (TextView) findViewById(R.id.tvDebug);
            tvDebug.setText(String.valueOf(ampValue));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
