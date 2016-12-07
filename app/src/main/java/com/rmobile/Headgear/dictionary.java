package com.rmobile.Headgear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class dictionary extends AppCompatActivity {

    ListView lvWords;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("finish", false)) finish();
        setContentView(R.layout.activity_dictionary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        lvWords = (ListView) findViewById(R.id.lvWords);
        String[] wordsArray=getResources().getStringArray(R.array.words);
        ArrayList<String> Temp = new ArrayList<String>(0);
        for(int i=0;i<wordsArray.length;i++) {
            Temp.add(i, wordsArray[i]);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Temp);
        lvWords.setAdapter(adapter);

    }

}
