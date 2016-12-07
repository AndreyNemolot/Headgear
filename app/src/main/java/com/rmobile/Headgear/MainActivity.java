package com.rmobile.Headgear;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.ArrayList;



public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    TextView tvPlayers;
    Button btnEnter;
    ListView lvPlayers;
    EditText etName;
    ArrayList<String> ComNam = new ArrayList<String>(0);
    int number = 0;
    ArrayAdapter<String> adapter;
    SwipeDismissList mSwipeList;
    private ActionBarDrawerToggle toggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstact);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        lvPlayers = (ListView) findViewById(R.id.lvPlayers);
        tvPlayers = (TextView) findViewById(R.id.tvPlayers);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(this);
        etName = (EditText) findViewById(R.id.etName);
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(13);
        etName.setFilters(fa);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ComNam);

        tvPlayers.setVisibility(View.INVISIBLE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ComNam.size() < 4) || (ComNam.size() % 2 != 0)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Snackbar.make(view, "Нужно ввести чётное количество игроков не менее 4.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {
                    //Intent intentName = new Intent(MainActivity.this, Four2.class);
                    Intent intent = new Intent(MainActivity.this, TwoactActivity.class);
                    intent.putExtra("mass", ComNam);
                    //intentName.putExtra("Key", ComNam);
                    startActivity(intent);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeList = new SwipeDismissList(lvPlayers, new SwipeDismissList.OnDismissCallback() {
            public SwipeDismissList.Undoable onDismiss(AbsListView listView, final int position)  {
                final String item = ComNam.get(position);
                ComNam.remove(position);
                number--;
                lvPlayers.setAdapter(adapter);
                return new SwipeDismissList.Undoable() {
                    @Override
                    public String getTitle() {
                        return "Игрок " + item + " удален";
                    }
                    @Override
                    public void undo() {
                        // Reinsert the item at its previous position.
                        ComNam.add(position, item);
                        number++;
                        lvPlayers.setAdapter(adapter);
                       // tvNumber.setText(String.valueOf(number));
                    }
                    @Override
                    public void discard() {
                        // Just write a log message (use logcat to see the effect)
                        Log.w("DISCARD", "item " + item + " now finally discarded");
                    }
                };
            }
        }, SwipeDismissList.UndoMode.SINGLE_UNDO);

        mSwipeList.setUndoMultipleString(null);
        mSwipeList.setRequireTouchBeforeDismiss(false);
        mSwipeList.setAutoHideDelay(2000);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnter:
                tvPlayers.setVisibility(View.VISIBLE);
                if(number<30) {
                    if (!etName.getText().toString().equals("")) {
                        ComNam.add(number, etName.getText().toString());
                        number++;
                        etName.setText("");
                        lvPlayers.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Ничего не введено, повторите ввод", Toast.LENGTH_LONG).show();
                    }
                    if (ComNam.size() == 1) {
                        Toast.makeText(this, "Чтобы удалить имя смахните его.", Toast.LENGTH_LONG).show();
                    }
                }else{Toast.makeText(this, "Введено максимальное количество игроков.", Toast.LENGTH_LONG).show();}
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //SomeActivity - имя класса Activity для которой переопределяем onBackPressed();
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Просто вызов
        if (toggle != null)
            toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (toggle != null)
            toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_library:
                startActivity(new Intent(this, dictionary.class));
                return true;
            case R.id.action_info:
                startActivity(new Intent(this, InfActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
