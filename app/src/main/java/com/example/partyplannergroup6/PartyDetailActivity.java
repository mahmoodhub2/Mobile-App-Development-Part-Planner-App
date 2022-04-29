//
// FILE          : PartyDetailActivity.java
// PROJECT       : MAD-A2
// PROGRAMMERS   : Evan Shouldice 8731443
//                 Mahmood Al-Zubaidi
//                 Nawriz Ibrahim
//                 Sohaib Sheikh
// FIRST VERSION : March 18, 2022
// DESCRIPTION   : This file contains the PartyDetailActivity class
//
package com.example.partyplannergroup6;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// CLASS NAME : PartyDetailActivity
// PURPOSE    :  To create new party entries or edit existing party entries
public class PartyDetailActivity extends AppCompatActivity
{
    private EditText partyNameEditText, descEditText, locationEditText;
    private DatePickerDialog datePickerDialog;
    private Button timeButton, dateButton, sendInvitesButton, deleteButton, viewMapButton;
    private Party selectedParty;
    //Clock variables
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_detail);
        //Declare functions onCreate
        initWidgets();
        checkForEditNote();

    }



    // FUNCTION    : initWidgets()
    // DESCRIPTION : Initializes editText Widgets and buttons
    // PARAMETERS  : none
    // RETURNS     : none
    private void initWidgets()
    {
        partyNameEditText = findViewById(R.id.partyNameEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        //partyTimeEditText = findViewById(R.id.partyTimeEditText);
        timeButton = findViewById(R.id.timeButton);
        initDatePicker();
        dateButton = findViewById(R.id.dateButton);
        dateButton.setText(getTodaysDate());
        sendInvitesButton = findViewById(R.id.sendInvitesButton);
        deleteButton = findViewById(R.id.deletePartyButton);
        viewMapButton = findViewById(R.id.viewMapButton);
    }




    // FUNCTION    : getTodaysDate()
    // DESCRIPTION : Gets a string of today's date
    // PARAMETERS  : none
    // RETURNS     : String date
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }



    // FUNCTION    : initDatePicker()
    // DESCRIPTION : Initialize the date picker
    // PARAMETERS  : none
    // RETURNS     : none
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1; //So January is not set to 0
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        // DEFAULT DATE - TODAY'S DATE
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }



    // FUNCTION    : makeDateString()
    // DESCRIPTION : Makes a string out of day, month, year
    // PARAMETERS  : int day, int month, int year
    // RETURNS     : String date
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }



    // FUNCTION    : getMonthFormat(int month)
    // DESCRIPTION : Changes int month to a string containing the name of the month
    // PARAMETERS  : int month
    // RETURNS     : String month
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //DEFAULT - shouldn't happen but just in case
        return "JAN";
    }



    // FUNCTION    : checkForEditNote()
    // DESCRIPTION : Checks to see if the current party is new or exists already.
    //               Shows delete button only when it is an existing entry.
    // PARAMETERS  : none
    // RETURNS     : none
    private void checkForEditNote()
    {
        Intent previousIntent = getIntent();
        int passedPartyID = previousIntent.getIntExtra(Party.PARTY_EDIT_EXTRA, -1);
        selectedParty = Party.getPartyForID(passedPartyID);

        if (selectedParty != null)
        {
            partyNameEditText.setText(selectedParty.getPartyName());
            descEditText.setText(selectedParty.getDescription());
            locationEditText.setText(selectedParty.getLocation());
            timeButton.setText(selectedParty.getPartyTime());
            dateButton.setText(selectedParty.getPartyDate());
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }



    // FUNCTION    : saveParty()
    // DESCRIPTION : Saves a new party or updates an existing one
    // PARAMETERS  : View view
    // RETURNS     : none
    public void saveParty(View view)
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String partyName = String.valueOf(partyNameEditText.getText());
        String desc = String.valueOf(descEditText.getText());
        String location = String.valueOf(locationEditText.getText());
        String partyTime = String.valueOf(timeButton.getText());
        String partyDate = String.valueOf(dateButton.getText());

        if (selectedParty == null)
        {
            int id = Party.partyArrayList.size();
            Party newParty = new Party(id, partyName, desc, location, partyTime, partyDate);
            Party.partyArrayList.add(newParty);
            sqLiteManager.addPartyToDatabase(newParty);
        }
        else
        {
            selectedParty.setPartyName(partyName);
            selectedParty.setDescription(desc);
            selectedParty.setLocation(location);
            selectedParty.setPartyTime(timeButton.getText().toString());
            selectedParty.setPartyDate(dateButton.getText().toString());
            sqLiteManager.updatePartyInDB(selectedParty);
        }

        // creating a custom broadcast //
        Intent intent = new Intent("com.party.Party_Saved");
        intent.putExtra("com.party.Party_Saved", "Party Saved");
        sendBroadcast(intent);

        finish();
    }



    // receives the outgoing broadcast and show a toast with the message that it carries
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // Function: onReceive
        // Description: this class Function recives the broadcasted message, and reveales it as a toast
        // Parameters:  Context context, Intent intent
        // Returns:     nothing
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedText = intent.getStringExtra("com.party.Party_Saved");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
        }
    };


    // Function: onStart
    // Description: It runs when this activity gets started, it creates a new filter for the broadcast.
    // Parameters:  none
    // Returns:     none
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("com.party.Party_Saved");
        registerReceiver(broadcastReceiver, filter);
    }


    // Function: onStop
    // Description: it gets executed when the activity goes in background, and it unregisters the broadcastReceiver.
    // Parameters: none
    // Returns: none
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }



    // FUNCTION    : deleteNode()
    // DESCRIPTION : It pops up a an acception for the user, and if they accept then it'll pop up a
    //               dialog to ensure that they want to delete the party's info
    // PARAMETERS  : View view
    // RETURNS     : none
    public void deleteNode(View view)
    {
        final Intent serviceIntent = new Intent(this, PartyDeletedService.class);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission("com.example.partyplannergroup6.permission.DEADLY_ACTIVITY") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"com.example.partyplannergroup6.permission.DEADLY_ACTIVITY"}, 1);
        }
        else {
            new AlertDialog.Builder(PartyDetailActivity.this)
                    .setMessage("Are you sure you want to delete the party ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteFromDB();
                            stopService(serviceIntent);
                            startService(serviceIntent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }



    // FUNCTION    : deleteFromDB()
    // DESCRIPTION : Deletes the party in which the Delete button is pressed
    // PARAMETERS  : none
    // RETURNS     : none
    public void deleteFromDB(){
        selectedParty.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updatePartyInDB(selectedParty);
    }



    // Function: onRequestPermissionsResult
    // Description: it checks to see if the user had accepted the permission or not, if so then it goes to the new activty.
    // Parameters: int requestCode, String[] permissions, int[] grantResults
    // Returns: void
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        final Intent serviceIntent = new Intent(this, PartyDeletedService.class);

        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(PartyDetailActivity.this)
                            .setMessage("Are you sure you want to delete the party ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteFromDB();
                                    stopService(serviceIntent);
                                    startService(serviceIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("Party Planner", "user refused to delete the item");
                                }
                            }).show();
                } else {
                    // disable functionality that depends on this permission.
                    Log.d("Party Planner", "permission denied");
                }
                return;
            }
        }
    }




    // FUNCTION    : popTimePicker()
    // DESCRIPTION : Creates a Time Picker for user to choose a time when they click on "Select Time" button
    // PARAMETERS  : View view
    // RETURNS     : none
    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }



    // FUNCTION    : openDatePicker()
    // DESCRIPTION : Displays datePickerDialog
    // PARAMETERS  : View view
    // RETURNS     : none
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }



    // FUNCTION    : sendInvites()
    // DESCRIPTION : Creates an intent and starts SendInvites activity.
    //               It includes some extra strings for filling out the invite fields in the new activity.
    // PARAMETERS  : View view
    // RETURNS     : none
    public void sendInvites(View view) {
        Intent intent = new Intent(this, SendInvites.class);
        intent.putExtra("subject", partyNameEditText.getText().toString());
        intent.putExtra("inviteDetails",
                        "You're Invited to a Party!\n" +
                                "Details: " + descEditText.getText().toString() +
                                "\nLocation: " + locationEditText.getText().toString() +
                                "\nTime: " + timeButton.getText().toString() +
                                "\nDate: " + dateButton.getText().toString());
        startActivity(intent);
    }


    // FUNCTION    : viewMap()
    // DESCRIPTION : Allows user to find a location with use of Google Maps.  It loads originally with Conestoga College.
    // PARAMETERS  : View view
    // RETURNS     : none
    public void viewMap(View view) {
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse("geo: 43.48044925335643, -80.5184361908345"));
        startActivity(intent);
    }
}