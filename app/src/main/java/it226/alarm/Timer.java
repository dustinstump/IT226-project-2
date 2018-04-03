package it226.alarm;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Timer extends AppCompatActivity {

    private EditText timerTime, timerText;
    private Button createTimer, cancelTimer;
    private String text;
    private GregorianCalendar time;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private Context context;
    private Notification notification_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        this.context = this;
        timerText = (EditText) findViewById(R.id.timerMessage);
        timerTime = (EditText) findViewById(R.id.timerTime);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        notification_popup = new Notification.Builder(this)
                .setContentTitle("Alarm!")
                .build();
    }


    private void createTimer() {
        timerText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                text = null;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    text = timerText.toString();
                    handled = true;
                }
                return handled;
            }
        });
        timerTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                time = null;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SimpleDateFormat format = new SimpleDateFormat();
                    time = new GregorianCalendar();
                    try {
                        time.setTime(format.parse(timerTime.toString()));
                    } catch (Exception e) {
                        time = null;
                        return false;
                    }
                    handled = true;
                }
                return handled;
            }
        });

        if (time != null && text != null) {
            completeTimer();
        }
    }

    private void deleteTimer() {
        if (alarmManager.getNextAlarmClock() != null) {
            alarmManager.cancel(alarmIntent);
        }

    }

    private void completeTimer() {
        long currentTime = System.currentTimeMillis();
        if (time.getTimeInMillis() <= currentTime) {
            time.setTimeInMillis(time.getTimeInMillis() + 365 * 86400000);
        }
        Intent intent = new Intent(context, AlarmReciever.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time.getTimeInMillis(), alarmIntent);
    }


    private class AlarmReciever implements AlarmManager.OnAlarmListener
    {
        @Override
        public void onAlarm(){
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            notificationManager.notify(0, notification_popup);
        }
    }

}