//
// FILE          : SQLiteManager.java
// PROJECT       : MAD-A2
// PROGRAMMERS   : Evan Shouldice 8731443
//                 Mahmood Al-Zubaidi
//                 Nawriz Ibrahim
//                 Sohaib Sheikh
// FIRST VERSION : March 18, 2022
// DESCRIPTION   : This file contains the SQLite manager class
//
package com.example.partyplannergroup6;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



// CLASS NAME : SQLiteManager extends SQLiteOpenHelper
// PURPOSE    : To manage the SQLite Database
public class SQLiteManager extends SQLiteOpenHelper
{
    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "PartyDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Party";
    private static final String COUNTER = "Counter";
    private static final String ID_FIELD = "id";
    private static final String PARTY_NAME_FIELD = "partyName";
    private static final String DESC_FIELD = "desc";
    private static final String LOCATION_FIELD = "location";
    private static final String PARTY_TIME_FIELD = "partyTime";
    private static final String PARTY_DATE_FIELD = "partyDate";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");



    // FUNCTION    : SQLiteManager(Context context)
    // DESCRIPTION :
    // PARAMETERS  :
    // RETURNS     :
    public SQLiteManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // FUNCTION    : instanceOfDatabase(Context context)
    // DESCRIPTION : creates instance of database or returns exisiting database
    // PARAMETERS  : Context context
    // RETURNS     : sqLiteManager
    public static SQLiteManager instanceOfDatabase(Context context)
    {
        if (sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }



    // FUNCTION    : onCreate(SQLiteDatabase sqLiteDatabase)
    // DESCRIPTION : creates a table in the sqLiteDatabase
    // PARAMETERS  : SQLiteDatabase sqLiteDatabase
    // RETURNS     : none
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        // CREATE TABLE IN DATABASE
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT,")
                .append(PARTY_NAME_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT, ")
                .append(LOCATION_FIELD)
                .append(" TEXT, ")
                .append(PARTY_TIME_FIELD)
                .append(" TEXT, ")
                .append(PARTY_DATE_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }



    // FUNCTION    : onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    // DESCRIPTION : could be used to upgrade table in future but leaving blank for now
    // PARAMETERS  : none
    // RETURNS     : none
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
    }



    // FUNCTION    : addPartyToDatabase(Party party)
    // DESCRIPTION : adds a new party to the database
    // PARAMETERS  : Party party
    // RETURNS     : none
    public void addPartyToDatabase(Party party)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, party.getId());
        contentValues.put(PARTY_NAME_FIELD, party.getPartyName());
        contentValues.put(DESC_FIELD, party.getDescription());
        contentValues.put(LOCATION_FIELD, party.getLocation());
        contentValues.put(PARTY_TIME_FIELD, party.getPartyTime());
        contentValues.put(PARTY_DATE_FIELD, party.getPartyDate());
        contentValues.put(DELETED_FIELD, getStringFromDate(party.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }



    // FUNCTION    : populatePartyListArray()
    // DESCRIPTION : queries the database for info and populates partyArrayList
    // PARAMETERS  : none
    // RETURNS     : none
    public void populatePartyListArray()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
        {
            if (result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(1);
                    String partyName = result.getString(2);
                    String desc = result.getString(3);
                    String location = result.getString(4);
                    String partyTime = result.getString(5);
                    String partyDate = result.getString(6);
                    String stringDeleted = result.getString(7);
                    Date deleted = getDateFromString(stringDeleted);
                    Party party = new Party (id, partyName, desc, location, partyTime, partyDate, deleted);
                    Party.partyArrayList.add(party);
                }
            }
        }
    }



    // FUNCTION    : updatePartyInDB(Party party)
    // DESCRIPTION : updates an already existing party in the database
    // PARAMETERS  : Party party
    // RETURNS     : none
    public void updatePartyInDB(Party party)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, party.getId());
        contentValues.put(PARTY_NAME_FIELD, party.getPartyName());
        contentValues.put(DESC_FIELD, party.getDescription());
        contentValues.put(LOCATION_FIELD, party.getLocation());
        contentValues.put(PARTY_TIME_FIELD, party.getPartyTime());
        contentValues.put(PARTY_DATE_FIELD, party.getPartyDate());
        contentValues.put(DELETED_FIELD, getStringFromDate(party.getDeleted()));
        // Update values with where statement
        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf((party.getId()))});
    }



    // FUNCTION    : getStringFromDate(Date date)
    // DESCRIPTION : turns date into string
    // PARAMETERS  : Date date
    // RETURNS     : null if date is null, otherwise a Date string
    private String getStringFromDate(Date date)
    {
        if(date == null)
            return null;

        return dateFormat.format(date);
    }



    // FUNCTION    : getDateFromString(String string)
    // DESCRIPTION : turns string into date
    // PARAMETERS  : String string
    // RETURNS     : Date if possible, otherwise null
    private Date getDateFromString(String string)
    {
        try
        {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e)
        {
            return null;
        }
    }
}