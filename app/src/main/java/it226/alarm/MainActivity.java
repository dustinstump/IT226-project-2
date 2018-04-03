package it226.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    Context context;
    Button alarm_clock;
    Button timer;
    Button location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        alarm_clock = (Button) findViewById(R.id.alarm_clock);
        alarm_clock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AlarmClock.class);
                startActivity(i);
            }
        });

        timer = (Button) findViewById(R.id.timer);
        timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent j = new Intent(getApplicationContext(), Timer.class);
                startActivity(j);
            }
        });

        location = (Button) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent k = new Intent(getApplicationContext(), LocationAlarm.class);
                startActivity(k);
            }
        });
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
