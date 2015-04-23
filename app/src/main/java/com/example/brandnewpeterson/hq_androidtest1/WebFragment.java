package com.example.brandnewpeterson.hq_androidtest1;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by brandnewpeterson on 4/18/15.
 */

public class WebFragment extends Fragment {

    private WebView wv;
    private Handler progressBarbHandler = new Handler();


    public WebFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);

        final ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBar);
        final TextView tv = (TextView) rootView.findViewById(R.id.textView);


        wv = (WebView) rootView.findViewById(R.id.webView);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        final Bundle bundle=getArguments();

        wv.getSettings().setJavaScriptEnabled(true);


        File file = new File(getActivity().getFilesDir() + "/" + bundle.getString("fileLabel") + ".html");

        if(file.exists()) {

            String path = "file://" + file.getAbsolutePath();
            int index = path.lastIndexOf(File.separator);
            String root = path.substring(index + 1);

            tv.setVisibility(View.VISIBLE);


            try {
                wv.loadUrl("file://" + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            wv.loadUrl(bundle.getString("urlString"));
            //Log.d("Testing", bundle.getString("urlString"));

        new Thread(new Runnable() {

            public void run() {
                while (pb.getProgress() < 100) {


                    // sleep 1/100th second
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            pb.setVisibility(View.INVISIBLE);
                            tv.setVisibility(View.INVISIBLE);

                        }
                    });
                }

            }
        }).start();


        return rootView;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

}