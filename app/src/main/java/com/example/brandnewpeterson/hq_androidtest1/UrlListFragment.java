    package com.example.brandnewpeterson.hq_androidtest1;

    import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

    /**
     * Created by brandnewpeterson on 4/19/15.
     */
    public class UrlListFragment extends ListFragment {
        private OnUrlSelectedListener mClickCallback;
        private DownloadHelper downloadHelper = new DownloadHelper();
        private JSONObject pages;

        @Override
        public void onDetach() {
            mClickCallback = null;
            super.onDetach();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            //Ensure that activity interface is implemented -- for listening for item clicks
            try {
                mClickCallback = (OnUrlSelectedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnUrlSelectedListener");
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //Get the JSON objects out of assets and parse.

            try {
                pages = downloadHelper.parseJSONAsset(getActivity());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> adapter = null;
                adapter = new ArrayAdapter<String>(
                        inflater.getContext(), android.R.layout.simple_list_item_1,
                        getURLLabels());

            setListAdapter(adapter);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            String label = ((TextView) v).getText().toString();

            JSONObject page = null;
            try {
                page = pages.getJSONObject(label);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = null;

            try {
                url = page.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Clean up url
            String cleanurl = downloadHelper.cleanUpUrl(url);

            mClickCallback.onPageSelected(cleanurl, label);

        }


        public interface OnUrlSelectedListener {
            public void onPageSelected(String urlString, String label);
        }

        private ArrayList<String> getURLLabels() {

            Iterator<String> key = pages.keys();

            ArrayList<String> urlStrings = new ArrayList<String>();

            while (key.hasNext()) {
                String keyString = key.next();

                if (!(keyString.contains("base") || keyString.contains("index")))
                    urlStrings.add(keyString);
            }

            return urlStrings;
        }

    }
