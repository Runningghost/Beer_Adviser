package com.ghost.dmac.beeradviser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class DisplayBeer extends AppCompatActivity {

    String url = "https://www.beerknurd.com/user#";
    String login = "http://www.beerknurd.com/api/tasted/list_user/468012";
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
                //URL url = new URL(params[0]);

               String useragent = System.getProperty("http.agent");

//               Connection.Response loginForm = Jsoup.connect(url)
//                        .method(Connection.Method.GET)
 //                       .execute();

               // Document doc = loginForm.parse();
                Connection.Response loginForm = Jsoup.connect(url)
//                Document document = Jsoup.connect(url)
                        .userAgent(useragent)
                        .data("username", user.username)
                        .data("password", user.password)
                        .data("op", "Log+in")
                        .data("form_build_id", "form-E6x907QMYIA4BUre26kq2hQnVYFxIsQb1ALF4LH7j4I")
                        .data("form_id", "custom_login_form")
//                        .post();
                        .method(Connection.Method.POST)
                        .execute();
                Document doc = loginForm.parse();
                String sessionId = loginForm.cookie("Set-Cookie");
//               Map<String, String> sessionId = loginForm.cookies();



                Document log = Jsoup.connect(url)
                        //.cookie("Cookie",sessionId)
                        .userAgent(useragent)
                        .ignoreContentType(true)
                        .get();


//                contentAsString = tasted.printNames(log.html());
               contentAsString = loginForm.toString();

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


