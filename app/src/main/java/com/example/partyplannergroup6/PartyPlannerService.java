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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

// CLASS NAME : PartyPlannerService
// PURPOSE    :  To create a notification indicating that the email has been sent
public class PartyPlannerService extends Service
{
    private static String Which_Channel = "My Party";

    NotificationManager NFManager = null;

    public PartyPlannerService()
    {
    }

    @Override
    public void onCreate()
    {
        Log.d("MyParty", "Party Planner Service Created");

        Intent notifyMyParty = new Intent(this, MainActivity.class);
        notifyMyParty.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int piFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notifyMyParty,piFlag);

        CharSequence cTitle = "Email has been sent";
        int icon = R.drawable.ic_launcher_foreground;

        // setting attributes for the notification
        NotificationCompat.Builder mBuilder
                = new NotificationCompat.Builder(this, Which_Channel)
                .setSmallIcon(icon)
                .setContentTitle(cTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NFManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "My Channel Name";
            String description = "My much larger channel description";
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(Which_Channel, name, NotificationManagerCompat.IMPORTANCE_DEFAULT);
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

    // Function: onBind
    // Description: To bind the service
    // Parameters: Intent intent
    // Returns: IBinder
    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    // Function: onStartCommand
    // Description: to response when startService is called
    // Parameters: Intent intent, int flags, int partyServiceID
    // Returns: START_STICKY, which leaves the service in the started state
    @Override
    public int onStartCommand(Intent intent, int flags, int partyServiceID)
    {
        Log.d("MyParty", "This is Party Planner ServiceID#s: " + partyServiceID);
        return START_STICKY;
    }

    // Function: onDestroy
    // Description: To stop the service when stopService calls it
    // Parameters: none
    // Returns: none
    @Override
    public void onDestroy()
    {
        Log.d("MyParty", "Party Planner Service has been Destroyed");
        NFManager.cancel(1);
    }
}