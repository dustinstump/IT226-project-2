package it226.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Dustin on 11/7/16.
 */

public class RingtonePlayingService extends Service{

    MediaPlayer player;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //fetch extra string values
        String state = intent.getExtras().getString("extra");

        //converts extra strings from the intent to start IDs (0 or 1)
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }


        if(!this.isRunning && startId == 1) {
            //create instance of media player
            player = MediaPlayer.create(this, R.raw.birdsound);
            player.setLooping(true);
            player.start();

            this.isRunning = true;
            this.startId = 0;

            //notification
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //set intent that goes to main activity
            Intent intent_main_activity = new Intent(this.getApplicationContext(), AlarmClock.class);
            //pending intent
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

            //notification parameters
            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("Alarm!").setContentText("Click here")
                    .setContentIntent(pending_intent_main_activity)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setAutoCancel(true)
                    .build();

            //notification call command
            nm.notify(0, notification_popup);
        }
        else if(this.isRunning && startId == 0) {
            player.stop();
            player.reset();

            this.isRunning = false;
            this.startId = 0;
        }
        else if(!this.isRunning && startId == 0) {
            this.isRunning = false;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 1) {
            this.isRunning = true;
            this.startId = 1;
        }
        else {

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.

        super.onDestroy();
        this.isRunning = false;
    }


}
