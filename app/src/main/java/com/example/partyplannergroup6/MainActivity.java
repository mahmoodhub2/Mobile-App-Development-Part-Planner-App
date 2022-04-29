//
// FILE          : MainActivity.java
// PROJECT       : MAD-A2
// PROGRAMMERS   : Evan Shouldice 8731443
//                 Mahmood Al-Zubaidi
//                 Nawriz Ibrahim
//                 Sohaib Sheikh
// FIRST VERSION : March 18, 2022
// DESCRIPTION   : This file contains the main activity for our Party Planner App
//
package com.example.partyplannergroup6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

// CLASS NAME :
// PURPOSE    :
public class MainActivity extends AppCompatActivity {

    private ListView partyListView;



    // FUNCTION    : onCreate()
    // DESCRIPTION : Called when activity starts to set up our app
    // PARAMETERS  : none
    // RETURNS     : none
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Declare functions onCreate
        initWidgets();
        loadFromDBToMemory();
        setPartyAdapter();
        setOnClickListener();
    }



    // FUNCTION    : initWidgets()
    // DESCRIPTION : Initializes widgets
    // PARAMETERS  : none
    // RETURNS     : none
    private void initWidgets()
    {
        partyListView = findViewById(R.id.partyListView);
    }



    // FUNCTION    : LoadFromDBToMemory()
    // DESCRIPTION : Loads info from database into Listview
    // PARAMETERS  : none
    // RETURNS     : none
    private void loadFromDBToMemory()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populatePartyListArray();
    }



    // FUNCTION    : setPartyAdapter()
    // DESCRIPTION : sets up PartyAdapter
    // PARAMETERS  : none
    // RETURNS     : none
    private void setPartyAdapter()
    {
        PartyAdapter partyAdapter = new PartyAdapter(getApplicationContext(), Party.nonDeletedParties());
        partyListView.setAdapter(partyAdapter);
    }



    // FUNCTION    : setOnClickListener()
    // DESCRIPTION :
    // PARAMETERS  : none
    // RETURNS     : none
    private void setOnClickListener()
    {
        partyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Party selectedParty = (Party) partyListView.getItemAtPosition(position);
                Intent editPartyIntent = new Intent(getApplicationContext(), PartyDetailActivity.class);
                editPartyIntent.putExtra(Party.PARTY_EDIT_EXTRA, selectedParty.getId());
                startActivity(editPartyIntent);
            }
        });
    }



    // FUNCTION    : newParty(View view)
    // DESCRIPTION : Used to create a new party
    // PARAMETERS  : View view
    // RETURNS     : none
    public void newParty(View view)
    {
        Intent newPartyIntent = new Intent(this, PartyDetailActivity.class);
        startActivity(newPartyIntent);
    }



    // FUNCTION    : onResume()
    // DESCRIPTION : Called when program resumes
    // PARAMETERS  : none
    // RETURNS     : none
    @Override
    protected void onResume()
    {
        super.onResume();
        setPartyAdapter();
    }

}