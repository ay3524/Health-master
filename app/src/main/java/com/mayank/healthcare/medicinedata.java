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

public class medicinedata extends AppCompatActivity {

    ArrayList<HashMap<String, String>> contactList;
    private String TAG = PredictorActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);


        Intent intent = getIntent();
        String data = intent.getStringExtra("medicine");
        String u = "https://phc-rest-api.herokuapp.com/getdetails/medicines?name=" + data;
        u = u.replaceAll(" ", "%20");

        url = u;

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listViewmedicinedata);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(medicinedata.this);
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
                    JSONArray diseases = jsonObj.getJSONArray("diseases");

                    // looping through All Contacts
                    for (int i = 0; i < diseases.length(); i++) {
                        JSONObject c = diseases.getJSONObject(i);

                        String medicine_name = c.getString("medicine_name");
                        String uses = c.getString("uses");
                        String side_effects = c.getString("side_effects");
                        String precautions = c.getString("precautions");
                        String overdose = c.getString("overdose");
                        String have_details = c.getString("have_details");

                        // Phone node is JSON Object

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("medicine_name", medicine_name);
                        contact.put("uses", uses);
                        contact.put("side_effects", side_effects);
                        contact.put("precautions", precautions);
                        contact.put("overdose", overdose);
                        contact.put("have_details", have_details);


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
                    medicinedata.this, contactList,
                    R.layout.list_medicine_detail, new String[]{"medicine_name", "uses", "side_effects", "precautions", "overdose",
            }, new int[]{R.id.medicine_name,R.id.uses,R.id.side_effects,R.id.precautions,R.id.overdose});
            lv.setAdapter(adapter);
        }

    }

}
