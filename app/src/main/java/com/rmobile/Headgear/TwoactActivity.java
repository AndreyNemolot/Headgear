package com.rmobile.Headgear;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;


import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


public class TwoactActivity extends ActionBarActivity implements View.OnClickListener {
    TextView tvName;
    Spinner spWords;
    ListView lvComands;
    int rand;
    String[] TempPlayer = new String[2];
    ArrayList<Comands> comands;
    ArrayList<String> names;
    String[] NewPlayer = new String[2];
    SimpleAdapter sAdapter;
    ArrayList<Map<String, String>> data;
    Map<String, String> m;
    final String ATTRIBUTE_NAME_TEAM = "team";
    final String ATTRIBUTE_NAME_FIRST = "first";
    final String ATTRIBUTE_NAME_LAST = "last";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twoact);
        if (getIntent().getBooleanExtra("finish", false)) this.finish();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Toast.makeText(this, "Нажмите на команду чтобы редактировать её", Toast.LENGTH_LONG).show();
        comands = new ArrayList<Comands>(0);
        tvName = (TextView) findViewById(R.id.tvName);
        Intent intent = getIntent();
        names = intent.getStringArrayListExtra("mass");
        spWords = (Spinner) findViewById(R.id.spWords);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.words_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWords.setAdapter(adapter);
        spWords.setPrompt("Количество слов");
        spWords.setSelection(1);
        int comandNumber = 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionMenu floatingActionsMenu;
        floatingActionsMenu = (FloatingActionMenu)findViewById(R.id.multiple_actions);

        FloatingActionButton action_a = (FloatingActionButton) findViewById(R.id.action_a);
        action_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TwoactActivity.this, ThreeActivity.class);
                intent.putExtra("mass", comands);
                intent.putExtra("words", spWords.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        final FloatingActionButton action_b = (FloatingActionButton) findViewById(R.id.action_b);
        action_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size=comands.size()*2*Integer.parseInt(spWords.getSelectedItem().toString());
                String[] wordsArray=getResources().getStringArray(R.array.words);
                ArrayList<String> Temp = new ArrayList<String>(0);
                final Random random = new Random();
                int r;
                for(int i=0;i<size;i++) {
                    r = random.nextInt(wordsArray.length+1);
                    Temp.add(i,wordsArray[r]);
                    if (i > 1) {
                        for (int j = 0; j < i; j++) {
                            if (Temp.get(j).equals(Temp.get(i))) {
                                do {
                                    r = random.nextInt(wordsArray.length+1);
                                    Temp.set(i,wordsArray[r]);
                                } while (Temp.get(j).equals(Temp.get(i)));
                            }
                        }
                    }
                    Temp.set(i,wordsArray[r]);
                }
                Intent intentFour = new Intent(TwoactActivity.this, Four2.class);
                intentFour.putExtra("mass", comands);
                intentFour.putExtra("words", Temp);
                intentFour.putExtra("numWords",size);
                intentFour.putExtra("comNumb",comands.size());
                startActivity(intentFour);
            }
        });

        while (names.size() > 0) {
            rand = new Random().nextInt(names.size());
            TempPlayer[0] = names.get(rand);
            names.remove(rand);
            rand = new Random().nextInt(names.size());
            TempPlayer[1] = names.get(rand);
            names.remove(rand);
            comands.add(new Comands(TempPlayer, comandNumber));
            comandNumber++;
        }

        lvComands = (ListView) findViewById(R.id.lvComands);
        data = new ArrayList<Map<String, String>>(comands.size());
        for (int i = 0; i < comands.size(); i++) {
            m = new HashMap<String, String>();
            m.put(ATTRIBUTE_NAME_TEAM, comands.get(i).toString());
            m.put(ATTRIBUTE_NAME_FIRST, comands.get(i).getFistName());
            m.put(ATTRIBUTE_NAME_LAST, comands.get(i).getSecondName());
            data.add(m);
        }
        String[] from = {ATTRIBUTE_NAME_TEAM, ATTRIBUTE_NAME_FIRST, ATTRIBUTE_NAME_LAST};
        int[] to = {R.id.tvTeamName, R.id.tvFirstName, R.id.tvLastName};

        sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        lvComands.setAdapter(sAdapter);

        lvComands.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View item, int position, long id) {
                selectItem(position);
            }

        });
    }

    private void selectItem(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите новые имена игроков");

        final EditText firstName = new EditText(this);
        final EditText secondName = new EditText(this);
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(13);
        firstName.setFilters(fa);
        secondName.setFilters(fa);
        firstName.setMaxEms(5);

        firstName.setInputType(InputType.TYPE_CLASS_TEXT);
        secondName.setInputType(InputType.TYPE_CLASS_TEXT);
        firstName.setText(comands.get(position).getFistName());
        secondName.setText(comands.get(position).getSecondName());
        firstName.setHint("Введите первое имя");
        secondName.setHint("Введите второе имя");
        LinearLayout lila1= new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL);
        lila1.addView(firstName);
        lila1.addView(secondName);
        builder.setView(lila1);

        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!(firstName.getText().toString().equals("")) && !(secondName.getText().toString().equals(""))) {
                    NewPlayer[0] = firstName.getText().toString();
                    NewPlayer[1] = secondName.getText().toString();
                    comands.set(position, new Comands(NewPlayer, position));
                    lvComands.setAdapter(sAdapter);

                    m = new HashMap<String, String>();
                    m.put(ATTRIBUTE_NAME_TEAM, "Команда" + (position + 1));
                    m.put(ATTRIBUTE_NAME_FIRST, firstName.getText().toString());
                    m.put(ATTRIBUTE_NAME_LAST, secondName.getText().toString());
                    data.set(position, m);
                }else{
                    Toast.makeText(TwoactActivity.this, "Ничего не введено, повторите ввод", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        comands.clear();
        names.clear();
    }

}
