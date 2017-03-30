package com.mayank.healthcare;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] member_names;
    TypedArray profile_pics;

    List<RowItem> rowItems;
    ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowItems = new ArrayList<>();

        member_names = getResources().getStringArray(R.array.member_names);

        profile_pics = getResources().obtainTypedArray(R.array.profile_pics);


        for (int i = 0; i < member_names.length; i++) {
            RowItem item = new RowItem(member_names[i],
                    profile_pics.getResourceId(i, -1));
            rowItems.add(item);
        }

        mylistview = (ListView) findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);

        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                switch (position) {
                    case 0:
                        Intent newActivity = new Intent(MainActivity.this, SymptomsNeedActivity.class);
                        startActivity(newActivity);
                        break;

                    case 1:
                        Intent newActivity2 = new Intent(MainActivity.this, Conditions.class);
                        startActivity(newActivity2);
                        break;

                    case 2:
                        Intent newActivity1 = new Intent(MainActivity.this, medicine.class);
                        startActivity(newActivity1);
                        break;
                }
            }
        });
    }
}