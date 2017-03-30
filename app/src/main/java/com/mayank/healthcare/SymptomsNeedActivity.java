package com.mayank.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SymptomsNeedActivity extends AppCompatActivity {
    EditText symptoms,age;
    Button search,clear;
    String symptomsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptomsneed);
        symptoms = (EditText) findViewById(R.id.predictions);
        age = (EditText) findViewById(R.id.agesymp);
        search = (Button) findViewById(R.id.search);
        clear = (Button) findViewById(R.id.clear);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                symptomsString = symptoms.getText().toString().trim();
                if(symptomsString.isEmpty()){
                    Toast.makeText(SymptomsNeedActivity.this, "Symptom's field empty!!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent box = new Intent(SymptomsNeedActivity.this, PredictorActivity.class);
                    box.putExtra("data", symptomsString);
                    startActivity(box);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symptoms.setText("");
                age.setText("");
            }
        });
    }
}
