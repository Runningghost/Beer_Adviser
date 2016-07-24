package com.ghost.dmac.beeradviser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.security.AccessController;


public class FindBeerActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String userid = "userid";
    public static String userNum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            com.google.android.gms.security.ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_find_beer);
        SharedPreferences ntest = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String newid = ntest.getString(userid, userNum);
        TextView numbers = (TextView) findViewById(R.id.userid);
        numbers.setText(newid);
    }

    public void onClickFindBeer(View view) {
        EditText userView = (EditText) findViewById(R.id.user);
        String userText = userView.getText().toString();
        EditText passView = (EditText) findViewById(R.id.pass);
        String passText = passView.getText().toString();
        //EditText userIdView = (EditText) findViewById(R.id.userid);
        //String userIdText = userIdView.getText().toString();

        
        Intent intent = new Intent(this, DisplayBeer.class);
        intent.putExtra("username", userText);
        intent.putExtra("password", passText);
        //if ( userIdText != null) {
        //    intent.putExtra("userid", userIdText);
        //}

        startActivity(intent);
    }


}




