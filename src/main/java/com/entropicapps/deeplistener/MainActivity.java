package com.entropicapps.deeplistener;

import android.content.Intent;
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

    int threshold;
    int interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final TextView tvDebug = (TextView) findViewById(R.id.tvDebug);
        final Button btStart = (Button) findViewById(R.id.btStart);
        final Button btStop = (Button) findViewById(R.id.btStop);
        btStop.setEnabled(false);
        final SeekBar barThreshold = (SeekBar) findViewById(R.id.sbarThreshold);
        final SeekBar barInterval = (SeekBar) findViewById(R.id.sbarInterval);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btStop.setEnabled(true);
                btStart.setEnabled(false);
                threshold = barThreshold.getProgress() + 50;
                interval = barInterval.getProgress() + 50;
                startService();

            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btStop.setEnabled(false);
                btStart.setEnabled(true);
                stopService();
            }
        });

    }

    public void startService() {
        //startService(new Intent(getBaseContext(), MainService.class).putExtra("threshold", threshold));
        Intent intent = new Intent(getBaseContext(), MainService.class);
        Bundle extras = new Bundle();
        extras.putInt("threshold", threshold);
        extras.putInt("interval", interval);
        intent.putExtras(extras);
        startService(intent);

    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), MainService.class));
    }

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
