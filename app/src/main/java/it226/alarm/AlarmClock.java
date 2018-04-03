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


public class AlarmClock extends AppCompatActivity {

    //Alarm manager
    AlarmManager am;
    TimePicker tp;
    TextView status;
    Context context;
    PendingIntent pending_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        
        this.context = this;

        //initialize alarm manager
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize timepicker
        tp = (TimePicker) findViewById(R.id.timePicker);

        //initialize status box
        status = (TextView) findViewById(R.id.status);
        //instance of calendar
        final Calendar calendar = Calendar.getInstance();

        //create intent for alarm receiver class
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        //initialize buttons
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        //create onClick listeners
        alarm_on.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //set calendar with time from timepicker
                calendar.set(Calendar.HOUR_OF_DAY, tp.getHour());
                calendar.set(Calendar.MINUTE, tp.getMinute());

                //get int value of timepicker selected time
                int hour = tp.getHour();
                int minute = tp.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }

                //update status box method
                alarm_text("Alarm set for " + hour_string + ":" + minute_string);
                //put extra string in my_intent
                //tells clock "set alarm" button pressed
                my_intent.putExtra("extra", "alarm on");
                //create pending intent that delays intent until user selects time
                pending_intent = PendingIntent.getBroadcast(AlarmClock.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //set alarm manager
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update status box method
                alarm_text("Alarm Dismissed");
                //cancel the alarm
                am.cancel(pending_intent);

                //put extra string into my_intent
                //tells clock "dismiss alarm" button pressed
                my_intent.putExtra("extra", "alarm off");

                //stop alarm audio
                sendBroadcast(my_intent);
            }
        });


    }

    private void alarm_text(String s) {

        status.setText(s);
    }
}



