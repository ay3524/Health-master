package com.mayank.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PredictorActivity extends AppCompatActivity {

    private String TAG = PredictorActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    private String url;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictor);


        Intent box = getIntent();
        String data = box.getStringExtra("data");
        String u = "https://phc-rest-api.herokuapp.com/checkSymptoms?symptoms=" + data;
        u = u.replaceAll(" ", "%20");

        url = u;

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listView);
        lv.setEmptyView(findViewById(R.id.empty_view));

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PredictorActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    final JSONArray disease_list = jsonObj.getJSONArray("disease_list");

                        // looping through All Contacts
                        for (int i = 0; i < disease_list.length(); i++) {
                            JSONObject c = disease_list.getJSONObject(i);

                            String disease_name = c.getString("disease_name");
                            String percentage = c.getString("percentage");


                            // Phone node is JSON Object

                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("disease_name", disease_name);
                            contact.put("percentage", percentage);


                            // adding contact to contact list
                            contactList.add(contact);
                        }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    PredictorActivity.this, contactList,
                    R.layout.list_item2, new String[]{"disease_name", "percentage",
            }, new int[]{R.id.disease_name,
                    R.id.percentage});

            lv.setAdapter(adapter);
        }

    }
}