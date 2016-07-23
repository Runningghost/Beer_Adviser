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
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;


public class DisplayBeer extends AppCompatActivity {

    String url = "https://www.beerknurd.com/user";
    String beer = "http://www.beerknurd.com/api/tasted/list_user/";
    String username = "username";
    String password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_beer_info);
        GetBeerInfo website = new GetBeerInfo();
        website.execute(url);
    }

    public class GetBeerInfo extends AsyncTask<String, String, String> {
        TextView beertaste = (TextView) findViewById(R.id.beer_tasted);
        Intent intent = getIntent();
        String userText = intent.getStringExtra(username);
        String passText = intent.getStringExtra(password);
        //String userNum = null;//intent.getStringExtra(userid);
        SharedPreferences sharedpreferences;



        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        @Override
        protected String doInBackground(String... params) {
            parse tasted = new parse();
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

                    Connection.Response loginForm = Jsoup.connect(url)
                            .data("username", userText)
                            .data("password", passText)
                            .data("op", "Log+in")
                            .data("form_id", "custom_login_form")
                            .userAgent(useragent)
                            .validateTLSCertificates(false)
                            .method(Connection.Method.POST)
                            .timeout(6000)
                            .execute();

                    Document log = Jsoup.connect(url)
                            .cookies(loginForm.cookies())
                            .userAgent(useragent)
                            .timeout(6000)
                            .get();

                    FindBeerActivity.userNum = tasted.userNum(log.body().className());

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(FindBeerActivity.userid, FindBeerActivity.userNum);

                    editor.apply();
                    }
                String id = sharedpreferences.getString(FindBeerActivity.userid, FindBeerActivity.userNum);

//                String userNum = log.body().className();
               Document tastedList = Jsoup.connect(beer + id)
                       .ignoreContentType(true)
                       .timeout(5000)
                       .get();

                contentAsString = tasted.printNames(tastedList.text());



//                contentAsString = log.html();
//                contentAsString = userNum;
                // Makes sure that the InputStream is closed after the app is
                // finished using it.

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


