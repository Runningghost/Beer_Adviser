package com.ghost.dmac.beeradviser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class FindBeerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_beer);
    }

    public void onClickFindBeer(View view) {
        EditText userView = (EditText) findViewById(R.id.user);
        String userText = userView.getText().toString();
        EditText passView = (EditText) findViewById(R.id.pass);
        String passText = passView.getText().toString();
        Intent intent = new Intent(this, DisplayBeer.class);
        intent.putExtra("username", userText);
        intent.putExtra("password", passText);

        startActivity(intent);
    }


}




