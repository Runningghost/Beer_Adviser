package com.ghost.dmac.beeradviser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DisplayBeer extends AppCompatActivity {

    String url = "https://www.beerknurd.com/user#";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_beer_info);
        GetBeerInfo website = new GetBeerInfo();
        website.execute(url);
    }

    public class GetBeerInfo extends AsyncTask<String, String, String> {
        TextView beertaste = (TextView) findViewById(R.id.beer_tasted);



        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        @Override
        protected String doInBackground(String... params) {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            String contentAsString = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                //int response = conn.getResponseCode();
                is = conn.getInputStream();

                // Convert the InputStream into a string
                contentAsString = readIt(is, len);
                //return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contentAsString;
        }

        public String readIt(InputStream stream, int len) throws IOException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
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


