//
// FILE          : Party.java
// PROJECT       : MAD-A2
// PROGRAMMERS   : Evan Shouldice 8731443
//                 Mahmood Al-Zubaidi
//                 Nawriz Ibrahim
//                 Sohaib Sheikh
// FIRST VERSION : March 18, 2022
// DESCRIPTION   : This file contains the Party Class
//
package com.example.partyplannergroup6;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

// CLASS NAME : Party
// PURPOSE    : To get and set Party Information
public class Party
{
    // ARRAY LIST
    public static ArrayList<Party> partyArrayList = new ArrayList<>();
    public static String PARTY_EDIT_EXTRA = "partyEdit";

    // VARIABLES
    private int id;
    private String partyName;
    private String description;
    private String location;
    private String partyTime;
    private String partyDate;
    private Date deleted;

    // CONSTRUCTORS
    public Party(int id, String partyName, String description, String location, String partyTime, String partyDate, Date deleted) {
        this.id = id;
        this.partyName = partyName;
        this.description = description;
        this.location = location;
        this.partyTime = partyTime;
        this.partyDate = partyDate;
        this.deleted = deleted;
    }

    public Party(int id, String partyName, String description, String location, String partyTime, String partyDate) {
        this.id = id;
        this.partyName = partyName;
        this.description = description;
        this.location = location;
        this.partyTime = partyTime;
        this.partyDate = partyDate;
    }

    // FUNCTION    : getPartyForID
    // DESCRIPTION : Checks if party ex
    // PARAMETERS  : int passedPartyID
    // RETURNS     : party if already exists, null if not
    public static Party getPartyForID(int passedPartyID)
    {
        for (Party party : partyArrayList)
        {
            if (party.getId() == passedPartyID)
                return party;
        }

        return null;
    }



    // FUNCTION    : nonDeletedParties()
    // DESCRIPTION : checks if party has a deleted date or not.
    //               If it does, it is deleted from the list.
    //               If the date is null, it is added back to the list.
    // PARAMETERS  : none
    // RETURNS     : nonDeleted
    public static ArrayList<Party> nonDeletedParties()
    {
        ArrayList<Party> nonDeleted = new ArrayList<>();
        for(Party party : partyArrayList)
        {
            if (party.getDeleted() == null)
                nonDeleted.add(party);
        }
        return nonDeleted;
    }



    // GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPartyTime() {
        return partyTime;
    }

    public void setPartyTime(String partyTime) {
        this.partyTime = partyTime;
    }

    public String getPartyDate() {
        return partyDate;
    }

    public void setPartyDate(String partyDate) {
        this.partyDate = partyDate;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}