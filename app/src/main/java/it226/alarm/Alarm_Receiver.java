package it226.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Dustin on 11/7/16.
 */

public class Alarm_Receiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        //fetch extra strings from the intent
        String get_the_string = intent.getExtras().getString("extra");

        //create intent to ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //pass extra string from main activity to ringtone playing service
        service_intent.putExtra("extra", get_the_string);

        //start the ringtone service
        context.startService(service_intent);
    }
}
