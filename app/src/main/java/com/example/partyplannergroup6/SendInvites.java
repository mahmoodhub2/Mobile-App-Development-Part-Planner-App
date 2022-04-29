package com.example.partyplannergroup6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class SendInvites extends AppCompatActivity
{

    private EditText guestEditText, subjectEditText, partyDetailsEditText;
    private Button sendEmailButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);
        guestEditText = findViewById(R.id.guestEditText);
        subjectEditText = findViewById(R.id.subjectEditText);
        partyDetailsEditText = findViewById(R.id.partyDetailsEditText);
        sendEmailButton = findViewById(R.id.sendEmailButton);

        getExtras();

    }



    // FUNCTION    : getExtras()
    // DESCRIPTION : Gets extra strings from bundle and sets text of subject and detail edit texts
    // PARAMETERS  : none
    // RETURNS     : none
    private void getExtras() {
        Bundle bundle = getIntent().getExtras();
        String subject = bundle.getString("subject");
        String inviteDetails = bundle.getString("inviteDetails");
        subjectEditText.setText(subject);
        partyDetailsEditText.setText(inviteDetails);

    }



    // FUNCTION    : sendEmail()
    // DESCRIPTION : Sets strings with values from guests, subject and partydetails edit texts
    //               Calls method to send email in outside application.
    // PARAMETERS  : none
    // RETURNS     : none
    public void sendEmail(View view)
    {
        String guests = guestEditText.getText().toString().trim();
        String subject = subjectEditText.getText().toString().trim();
        String message = partyDetailsEditText.getText().toString().trim();

        sendEmail(guests, subject, message);
    }



    // FUNCTION    : sendEmail()
    // DESCRIPTION : Sets user  up with invite emails in an outside application
    // PARAMETERS  : String guests, String subject, String message
    // RETURNS     : none
    private void sendEmail(String guests, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        // GUESTS, SUBJECT, MESSAGE
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{guests}); // array to separate emails by comma (,)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try
        {
            // Will prompt with choice of email provider and transfer the entered information to the email client
            startActivity(Intent.createChooser(emailIntent, "Choose an Email Client"));
        }
        catch (Exception e)
        {
            //no internet or email client unavailable
            Toast.makeText(this, e.getMessage(), Toast. LENGTH_SHORT).show();
        }
    }



    // Function: sentEmail
    // Description: Initially it stops the service if it is still runing, so that it starts it again. Then it travels the user back to the party-details slide
    // Parameters: none
    // Returns: none
    public void sentEmail(){
        final Intent partyPlannerServiceIntent = new Intent(this, PartyPlannerService.class);
        stopService(partyPlannerServiceIntent);
        startService(partyPlannerServiceIntent);


        Intent intent = new Intent(this, PartyDetailActivity.class);
        startActivity(intent);
    }
}