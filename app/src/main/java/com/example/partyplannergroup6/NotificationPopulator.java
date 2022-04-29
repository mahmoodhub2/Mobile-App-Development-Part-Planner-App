package com.example.partyplannergroup6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationPopulator extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String receivedText = intent.getStringExtra("com.party.Party_Deleted");
        Toast.makeText(context, "Party Deleted", Toast.LENGTH_SHORT).show();

        Log.i("MyApp", receivedText);
        context.stopService(new Intent(context, PartyDeletedService.class));
    }

}
