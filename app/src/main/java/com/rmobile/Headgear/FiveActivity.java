package com.rmobile.Headgear;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FiveActivity extends ActionBarActivity implements View.OnClickListener {

    ArrayList<Comands> comand;
    TextView tvScore;
    ListView lvResult;
    int Stage;
    FloatingActionButton fab;
    SimpleAdapter sAdapterHead;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        if (getIntent().getBooleanExtra("finish", false)) finish();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvScore = (TextView) findViewById(R.id.tvScore);
        lvResult = (ListView) findViewById(R.id.lvResult);
        comand = getIntent().getParcelableArrayListExtra("comands");
        Stage = getIntent().getIntExtra("Stage",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String ATTRIBUTE_NAME_TEAM = "team";
        final String ATTRIBUTE_NAME_FIRST = "first";
        final String ATTRIBUTE_NAME_LAST = "last";
        final String ATTRIBUTE_NAME_SCORE = "score";
        final String ATTRIBUTE_NAME_TEAM_HEAD = "team";
        final String ATTRIBUTE_NAME_SCORE_HEAD = "score";
        final String ATTRIBUTE_NAME_HEAD = "name";

        lvResult = (ListView) findViewById(R.id.lvResult);
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(comand.size());
        ArrayList<Map<String, String>> data_head = new ArrayList<Map<String, String>>(1);
        Map<String, String> m;
        m = new HashMap<String, String>();
        m.put(ATTRIBUTE_NAME_TEAM_HEAD, "Команда");
        m.put(ATTRIBUTE_NAME_SCORE_HEAD, "Очки");
        m.put(ATTRIBUTE_NAME_HEAD, "Игроки");
        data_head.add(m);
        String[] fromhead = {ATTRIBUTE_NAME_TEAM_HEAD, ATTRIBUTE_NAME_SCORE_HEAD, ATTRIBUTE_NAME_HEAD};
        int[] tohead = {R.id.tvKomand5, R.id.tvScore5, R.id.tvName5};
        sAdapterHead = new SimpleAdapter(this, data_head, R.layout.itemhead5, fromhead, tohead);
        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(sAdapterHead);

        for (int i = 0; i < comand.size(); i++) {
            m = new HashMap<String, String>();
            m.put(ATTRIBUTE_NAME_TEAM, comand.get(i).toString());
            m.put(ATTRIBUTE_NAME_FIRST, comand.get(i).getFistName());
            m.put(ATTRIBUTE_NAME_LAST, comand.get(i).getSecondName());
            m.put(ATTRIBUTE_NAME_SCORE, String.valueOf(comand.get(i).getScore()));
            data.add(m);
        }

        Map<String, String> temp = new HashMap<String, String>();
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = 0; j < data.size() - i - 1; j++) {
                if (Integer.parseInt(data.get(j).get(ATTRIBUTE_NAME_SCORE)) < Integer.parseInt(data.get(j+1).get(ATTRIBUTE_NAME_SCORE))) {
                    temp = data.get(j);
                    data.set(j, data.get(j+1));
                    data.set(j+1, temp);
                }
            }
        }

        String[] from = {ATTRIBUTE_NAME_SCORE, ATTRIBUTE_NAME_TEAM, ATTRIBUTE_NAME_FIRST, ATTRIBUTE_NAME_LAST};
        int[] to = {R.id.tvScore, R.id.tvTeamName, R.id.tvFirstName, R.id.tvLastName};

        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        mergeAdapter.addAdapter(sAdapter);
        lvResult.setAdapter(mergeAdapter);

        if (Stage==4) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_fiber_new_white_24dp));
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stage==4){
                    Intent intent = new Intent(FiveActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("finish", true);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}
