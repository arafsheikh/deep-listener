package com.entropicapps.deeplistener;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    double FIRST_VALUE = 0;
    double NEXT_VALUE = 0;
    double DIFFERENCE = 0;
    int threshold;

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
        final SeekBar barThreshold = (SeekBar) findViewById(R.id.sbarThreshold);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.start();
                btStop.setEnabled(true);
                btStart.setEnabled(false);
                threshold = barThreshold.getProgress();

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
        FIRST_VALUE = recorder.getAmplitude();
        mHandler.post(mRunnable);
    }

    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            TextView tvDebug = (TextView) findViewById(R.id.tvDebug);
            DIFFERENCE = FIRST_VALUE - NEXT_VALUE;
            NEXT_VALUE = FIRST_VALUE;
            tvDebug.setText(String.valueOf(DIFFERENCE));

            if (DIFFERENCE >= threshold) {
                Toast.makeText(getApplicationContext(), "Threshold exceeded", Toast.LENGTH_SHORT).show();
            }

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
