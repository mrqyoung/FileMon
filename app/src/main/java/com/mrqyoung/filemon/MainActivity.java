package com.mrqyoung.filemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends Activity {

    private TextView logTextView;
    private ToggleButton mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
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


    private void initViews() {
        logTextView = (TextView) findViewById(R.id.log_text);
        mainButton = (ToggleButton) findViewById(R.id.btn_main);
        //mainButton.setText(MonService.isRunning ? getString(R.string.stop_hint) : getString(R.string.start_hint));
        mainButton.setChecked(MonService.isRunning);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MonService.isRunning) {
                    Toast.makeText(MainActivity.this, "Start mon...", Toast.LENGTH_LONG).show();
                    startService(new Intent(MainActivity.this, MonService.class));
                } else {
                    Toast.makeText(MainActivity.this, "Stop.", Toast.LENGTH_LONG).show();
                    //stopService(new Intent(MainActivity.this, MonService.class));
                    Intent intentClickToStopMe = new Intent(MainActivity.this, NotificationClickedReceiver.class);
                    intentClickToStopMe.setAction(NotificationClickedReceiver.actionStopMe);
                    sendBroadcast(intentClickToStopMe);
                    loadLogs();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        if (!MonService.isRunning) loadLogs();
        super.onResume();
    }

    private void loadLogs() {
        String logFile = this.getExternalFilesDir(null).getPath() + "/" + Log.logFile;
        StringBuilder ret = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(logFile));
            String line;
            while ((line = reader.readLine()) != null) {
                ret.append(line).append('\n');
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logTextView.setText(ret.toString());
    }

}
