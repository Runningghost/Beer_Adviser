package com.ghost.dmac.beeradviser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;


public class DisplayBeer extends AppCompatActivity {

    String loginUrl = "https://www.beerknurd.com/user";
    String tastedBeerUrl = "http://www.beerknurd.com/api/tasted/list_user/";
    String username = "username";
    String password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_beer_info);
        GetBeerInfo website = new GetBeerInfo();
        website.execute(loginUrl);
    }

    public class GetBeerInfo extends AsyncTask<String, String, String> {
        TextView beertaste = (TextView) findViewById(R.id.beer_tasted);
        Intent intent = getIntent();
        String userText = intent.getStringExtra(username);
        String passText = intent.getStringExtra(password);
        //String userNum = null;//intent.getStringExtra(userid);
        SharedPreferences sharedpreferences;


        @Override
        protected String doInBackground(String... params) {
            BeerUtil tasted = new BeerUtil();
            sharedpreferences = getSharedPreferences(FindBeerActivity.MyPREFERENCES, Context.MODE_PRIVATE);


            String contentAsString = null;
            try {

               if (sharedpreferences.getString(FindBeerActivity.userid, FindBeerActivity.userNum) == null || !userText.isEmpty()) {

                    String useragent = System.getProperty("http.agent");

                   try {
                       HttpsURLConnection.setDefaultSSLSocketFactory(new TLSSocketFactory());
                   } catch(KeyManagementException | NoSuchAlgorithmException e) {
                       e.printStackTrace();
                   }

                    Connection.Response loginForm = Jsoup.connect(loginUrl)
                            .data("username", userText)
                            .data("password", passText)
                            .data("op", "Log+in")
                            .data("form_id", "custom_login_form")
                            .userAgent(useragent)
                            .validateTLSCertificates(false)
                            .method(Connection.Method.POST)
                            .timeout(6000)
                            .execute();

                    Document SaucerHomeScreen = Jsoup.connect(loginUrl)
                            .cookies(loginForm.cookies())
                            .userAgent(useragent)
                            .timeout(6000)
                            .get();

                    FindBeerActivity.userNum = tasted.userNum(SaucerHomeScreen.body().className());

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(FindBeerActivity.userid, FindBeerActivity.userNum);
                    editor.apply();
                    }
                String tastedBeerId = sharedpreferences.getString(FindBeerActivity.userid, FindBeerActivity.userNum);


               Document tastedList = Jsoup.connect(tastedBeerUrl + tastedBeerId)
                       .ignoreContentType(true)
                       .timeout(5000)
                       .get();

                contentAsString = tasted.printNames(tastedList.text());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return contentAsString;
        }

            @Override
        protected void onPostExecute(String result) {

            // execution of result of Long time consuming operation
            beertaste.setText(result);
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        @Override
        protected void onProgressUpdate(String... text) {
            beertaste.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }

}


