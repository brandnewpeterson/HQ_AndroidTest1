package com.example.brandnewpeterson.hq_androidtest1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity implements UrlListFragment.OnUrlSelectedListener {
    public static final String JSON_DATA = "http://appcontent.hotelquickly.com/en/1/android/index.json";
    public static final String CSS1 = "/css/styles.css";
    public static final String CSS2 = "/css/1/android.css";
    public static final String FONT1 = "/font/proximanova-light-webfont.ttf";
    public static final String FONT2 = "/font/proximanova-regular-webfont.ttf";
    public static final String FONT3 = "/font/proximanova-semibold-webfont.ttf";
    public static final String DOMAIN = "http://appcontent.hotelquickly.com";

    private JSONObject pages;

    private DownloadHelper downloadHelper = new DownloadHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new UrlListFragment())
                    .commit();
        }

        try {
            pages = downloadHelper.parseJSONAsset(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new CacheRemoteFormatFiles().execute();

    }

    public void onPageSelected(String urlString, String label) {

        //Bundle json obj
        Bundle bundle = new Bundle();
        bundle.putString("urlString", urlString);
        bundle.putString("fileLabel", label);

        // Create new fragment and transaction
        Fragment webFrag = new WebFragment();
        webFrag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, webFrag)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preloadWebViews() throws IOException {

        final Iterator<String> key = pages.keys();

        while (key.hasNext()) {

            String url = key.next();

            JSONObject obj = null;
            try {
                obj = pages.getJSONObject(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Boolean cache = null;
            try {
                cache = obj.getBoolean("cache");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String cleanUrl = null;

            if (cache) {

                try {
                    cleanUrl = downloadHelper.cleanUpUrl(obj.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new DownloadPage(url).execute(cleanUrl);

            }
        }
    }

    public class DownloadPage extends AsyncTask<String, Void, String> {

        String label;

        public DownloadPage(String label) {
            this.label = label;
        }

        protected String doInBackground(String... urls) {
            String webPageAsString = null;

            try {
                webPageAsString = downloadHelper.downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return webPageAsString;
        }

        protected void onPostExecute(String result) {

            File htmlFile = new File(getFilesDir(), label + ".html");
            try {
                downloadHelper.saveOutFile(htmlFile, result);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public class CacheRemoteFormatFiles extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {//After we've got the format files, start pre-caching pages.
            try {
                preloadWebViews();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                downloadFormatFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void downloadFormatFiles() throws IOException {

        String[] files = new String[]{CSS1, CSS2, FONT1, FONT2, FONT3};

        String content = null;
        for (String f : files) {

            int index = f.lastIndexOf(File.separator);
            String fileName = f.substring(index + 1);


            File path = new File(getFilesDir() + File.separator + f.replace(File.separator + fileName, "").toString());

            path.mkdirs();

            content = null;
            try {
                content = downloadHelper.downloadUrl(DOMAIN + f);
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(getFilesDir() + File.separator + f);

            downloadHelper.saveOutFile(file, content);
        }

    }


}
