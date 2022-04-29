package com.example.partyplannergroup6;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



public class HomePage extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }



    // FUNCTION    : goToMainPage()
    // DESCRIPTION : Takes user to MainActivity, but first it got to check to see if the user had granted a permission or not.
    //               If they did not, Then It'll pop up a permission dialog for them.
    // PARAMETERS  : View view
    // RETURNS     : none
    public void goToMainPage(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }


    // FUNCTION    : partyIdeas()
    // DESCRIPTION : Takes user to outside webpage for party ideas when icon clicked
    // PARAMETERS  : View view
    // RETURNS     : none
    public void partyIdeas(View view)
    {
        Intent webPageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://allthepartyideas.com/"));
        startActivity(webPageIntent);
    }


    public void gotoLink(View view){
        Intent intent = new Intent(this, FoodFeed.class);
        startActivity(intent);
    }



    // Function: onRequestPermissionsResult
    // Description: it checks to see if the user had accepted the permission or not, if so then it goes to the new activty.
    // Parameters: int requestCode, String[] permissions, int[] grantResults
    // Returns: void
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // disable functionality that depends on this permission.
                    Log.d("Party Planner", "permission denied");
                }
                return;
            }
        }
    }

}


