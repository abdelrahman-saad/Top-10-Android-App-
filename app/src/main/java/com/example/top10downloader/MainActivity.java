package com.example.top10downloader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listapps;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedlimit = 10;
    private String feedCachedUrl = "INVALIDATE";
    public static final String STATE_URL = "feedUrl";
    public static final String STATE_LIMIT = "feedlimit";
    TextView title;
public static ParseApplication PA ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listapps = findViewById(R.id.xmlListview);
        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL);
            feedlimit = savedInstanceState.getInt(STATE_LIMIT);

        }
        downloadUrl(String.format(feedUrl, feedlimit));
        title = findViewById(R.id.textView3);
listapps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         openItem( position );
    }
});

    }
    public void openItem( int position ) {
        Intent intent = new Intent(this , Listitem.class);
        intent.putExtra( " position" , position );

        startActivity(intent);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        if (feedlimit == 10) {
            menu.findItem(R.id.mnu10).setChecked(true);
        } else {
            menu.findItem(R.id.mnu25).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnuFree:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                title.setText("Top Free Applications");
                break;
            case R.id.mnuPaid:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                title.setText("Top Paid Applications");
                break;
            case R.id.mnuSongs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                title.setText("Top Songs");
                break;
            case R.id.mnuMovies:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";
                title.setText("Top Movies");
                break;
            case R.id.mnuTVEpi:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml";
                title.setText("Top TV Episodes");
                break;
            case R.id.mnuAlbums:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=%d/xml";
                title.setText("Top Albums");
                break;
            case R.id.mnu10:
            case R.id.mnu25:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    feedlimit = 35 - feedlimit;
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " setting feed limit to " + feedlimit);
                } else {
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " feed limit unchanged");
                }
                break;
            case R.id.mnuRefresh:
                feedCachedUrl = "INVALIDATE";
                break;
            default:
        }
        downloadUrl(String.format(feedUrl, feedlimit));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_URL, feedUrl);
        outState.putInt(STATE_LIMIT, feedlimit);
        super.onSaveInstanceState(outState);
    }

    private void downloadUrl(String feedUrl) {
        if (!feedUrl.equalsIgnoreCase(feedCachedUrl)) {


            Log.d(TAG, "downloadUrl: Starting Asynctask");
            DownloadData downloadDatat = new DownloadData();
            downloadDatat.execute(feedUrl);
            Log.d(TAG, "downloadUrl: Done");
        } else
            Log.d(TAG, "downloadUrl: URL not changed");
    }
    // ***************************************************new class ********************************


    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParseApplication parseApplication = new ParseApplication();
            parseApplication.parse(s);
            PA= parseApplication;
//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(MainActivity.this, R.layout.list_item, parseApplication.getApplications());
//            listapps.setAdapter(arrayAdapter);

            FeedAdapter<FeedEntry> feedAdapter = new FeedAdapter<>(MainActivity.this, R.layout.list_record, parseApplication.getApplications());
            listapps.setAdapter(feedAdapter);

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: Starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null)
                Log.e(TAG, "doInBackground: Error Downloading");

            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
//            InputStream inputStream = connection.getInputStream();
//           InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader reader = new BufferedReader(inputStreamReader);
                // this line makes the same function

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int CharsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    CharsRead = reader.read(inputBuffer);
                    if (CharsRead < 0)
                        break;
                    if (CharsRead > 0)
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, CharsRead));
                }
                reader.close();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: Security Exception. Needs permission" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data" + e.getMessage());
                e.printStackTrace();
            }
            return xmlResult.toString();
            //   return null;
        }
    }
}
