package com.example.partyplannergroup6;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.widget.Toast;
import android.net.ConnectivityManager;

public class ConnBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "Start Completed", Toast.LENGTH_SHORT).show();
        }
    }
}
