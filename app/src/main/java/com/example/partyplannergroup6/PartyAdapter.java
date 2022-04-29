//
// FILE          : PartyAdapter.java
// PROJECT       : MAD-A2
// PROGRAMMERS   : Evan Shouldice 8731443
//                 Mahmood Al-Zubaidi
//                 Nawriz Ibrahim
//                 Sohaib Sheikh
// FIRST VERSION : March 18, 2022
// DESCRIPTION   : This file contains the Party Adapter class
//
package com.example.partyplannergroup6;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;


// CLASS NAME  : Party Adapter
// DESCRIPTION :
public class PartyAdapter extends ArrayAdapter<Party>
{

    // FUNCTION    : PartyAdapter
    // DESCRIPTION : used to call the parent constructor
    // PARAMETERS  : Context context, List<Party> parties
    // RETURNS     : none
    public PartyAdapter(Context context, List<Party> parties)
    {
        super(context, 0, parties);
    }


    // FUNCTION    : getView()
    // DESCRIPTION : Fills the TextViews in our ListView
    // PARAMETERS  : int position, @Nullable View convertView, @NonNull ViewGroup parent
    // RETURNS     : convertView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Party party = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.party_cell, parent, false);

        TextView partyName = convertView.findViewById(R.id.cellPartyName);
        TextView desc = convertView.findViewById(R.id.cellDesc);
        TextView location = convertView.findViewById(R.id.cellLocation);
        TextView partyTime = convertView.findViewById(R.id.cellTime);
        TextView partyDate = convertView.findViewById(R.id.cellDate);

        partyName.setText(party.getPartyName());
        desc.setText(party.getDescription());
        location.setText(party.getLocation());
        partyTime.setText(party.getPartyTime().toString());
        partyDate.setText(party.getPartyDate().toString());

        return convertView;
    }

}
