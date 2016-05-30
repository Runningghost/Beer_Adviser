package com.ghost.dmac.beeradviser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;


public class DisplayBeer extends AppCompatActivity {

    String url = "https://www.beerknurd.com/user";
    String beer = "http://www.beerknurd.com/api/tasted/list_user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_beer_info);
        GetBeerInfo website = new GetBeerInfo();
        website.execute(url);
    }

    public class GetBeerInfo extends AsyncTask<String, String, String> {
        TextView beertaste = (TextView) findViewById(R.id.beer_tasted);
        Login user = new Login();


        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        @Override
        protected String doInBackground(String... params) {
            parse tasted = new parse();

            String contentAsString = null;
            try {

               String useragent = System.getProperty("http.agent");


                Connection.Response loginForm = Jsoup.connect(url)
                        .data("username", user.username)
                        .data("password", user.password)
                        .data("op", "Log+in")
                        .data("form_id", "custom_login_form")
                        .userAgent(useragent)
                        .method(Connection.Method.POST)
                        .timeout(6000)
                        .execute();
//                Document doc = loginForm.parse();
//               Map<String, String> sessionId = loginForm.cookies();



                Document log = Jsoup.connect(url)
                        .cookies(loginForm.cookies())
                        .userAgent(useragent)
                        .timeout(6000)
                        .get();

                String userNum = tasted.userNum(log.body().className());
//                String userNum = log.body().className();
               Document tastedList = Jsoup.connect(beer + userNum)
                       .ignoreContentType(true)
                       .timeout(5000)
                       .get();

                contentAsString = tasted.printNames(tastedList.text());
//               contentAsString = String.valueOf(sessionId);
//                Elements userNum = log.select("input#loaded_user");


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


