package com.example.partyplannergroup6;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;
import static android.app.Notification.EXTRA_NOTIFICATION_ID;

// CLASS NAME : PartyDeletedService
// PURPOSE    :  To create a notification indicating that the party has been deleted
public class PartyDeletedService extends Service {
    NotificationManager NFManager = null;

    public PartyDeletedService() {
    }
    public void onCreate() {
        Log.i("PartySavedService", "PartySavedService Created");

        Intent snoozeIntent = new Intent(this, NotificationPopulator.class);
        snoozeIntent.setAction("com.party.Party_Deleted");
        snoozeIntent.putExtra("Info", "Party Saved");
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        this.sendBroadcast(snoozeIntent);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);



        Intent notifyMyParty = new Intent(this, MainActivity.class);
        notifyMyParty.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int piFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notifyMyParty,piFlag);

        CharSequence tTxt = "Ticker Text";
        CharSequence cTitle = "The party has been deleted";
       // CharSequence cTxt = "Content Text";
        int icon = R.drawable.ic_launcher_foreground;

        NotificationCompat.Builder mBuilder
                = new NotificationCompat.Builder(this, "My Party")
                .setSmallIcon(icon)
                .setContentTitle(cTitle)
                //.setContentText(cTxt)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NFManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "My Channel Name";
            String description = "My much larger channel description";
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel("My Party", name, NotificationManagerCompat.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system
            NFManager.createNotificationChannel(channel);
        }


        NFManager.notify(1,mBuilder.build());

        ConnectivityManager conManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            Log.d("MyParty","Connection is Successful");
        }
    }



    // Function: onStartCommand
    // Description: to response when startService is called
    // Parameters: Intent intent, int flags, int partyServiceID
    // Returns: START_STICKY, which leaves the service in the started state
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyApp", "Service Started with id=" + startId);
        return START_STICKY;
    }


    // Function: onBind
    // Description: To bind the service
    // Parameters: Intent intent
    // Returns: IBinder
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }



    // Function: onDestroy
    // Description: To stop the service when stopService calls it
    // Parameters: none
    // Returns: none
    @Override
    public void onDestroy() {
        Log.i("MyApp", "Service Destroyed");
    }
}
