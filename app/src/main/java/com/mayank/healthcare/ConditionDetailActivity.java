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

import static com.mayank.healthcare.R.id.questions;

public class ConditionDetailActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> contactList;
    private String TAG = PredictorActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_detail);


        Intent intent = getIntent();
        String data = intent.getStringExtra("disease");
        String u = "https://phc-rest-api.herokuapp.com/getdetails/diseases?name=" + data;
        u = u.replaceAll(" ", "%20");

        url = u;

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listViewdiseasedata);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ConditionDetailActivity.this);
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

                        String disease_name = c.getString("disease_name");
                        String expectation = c.getString("expectation");
                        String questions = c.getString("questions");
                        String treatments = c.getString("treatments");
                        String risks = c.getString("risks");
                        String symptoms = c.getString("symptoms");
                        String how_common = c.getString("how_common");
                        String selfcare = c.getString("selfcare");
                        String when_to_see_doctor = c.getString("when_to_see_doctor");
                        String diagnosed_by = c.getString("diagnosed_by");
                        String made_worse_by = c.getString("made_worse_by");

                        // Phone node is JSON Object

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("disease_name", disease_name);
                        contact.put("expectation", expectation);
                        contact.put("questions", getTheString(questions));
                        contact.put("treatments", getTheString(treatments));
                        contact.put("risks", getTheString(risks));
                        contact.put("symptoms", getTheString(symptoms));
                        contact.put("how_common", how_common);
                        contact.put("selfcare", selfcare);
                        contact.put("when_to_see_doctor", when_to_see_doctor);
                        contact.put("diagnosed_by", diagnosed_by);
                        contact.put("made_worse_by", made_worse_by);
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
             **/
            ListAdapter adapter = new SimpleAdapter(
                    ConditionDetailActivity.this, contactList,
                    R.layout.condition_detail_item, new String[]{"disease_name", "expectation", "questions", "treatments", "risks",
                    "symptoms", "how_common", "selfcare", "when_to_see_doctor", "diagnosed_by", "made_worse_by"
            }, new int[]{R.id.disease_name, R.id.expectation, questions, R.id.treatments, R.id.risks, R.id.symptoms, R.id.how_common, R.id.selfcare,
                    R.id.when_to_see_doctor, R.id.diagnosed_by, R.id.made_worse_by});
            lv.setAdapter(adapter);
        }
    }

    private String getTheString(String s) {

        String s1 = s.replaceAll("\\[", "");
        String s2 = s1.replaceAll("\\]", "");
        String s3 = s2.replaceAll(",", "\n");
        String s4 = s3.replaceAll("\"", "");

        return s4;
    }


}
