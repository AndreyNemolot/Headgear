package com.rmobile.Headgear;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ThreeActivity extends ActionBarActivity implements View.OnClickListener {

    ArrayList<String> Temp = new ArrayList<String>(0);
    ArrayList<String> buf = new ArrayList<String>(0);
    TextView tvNameMember;
    EditText etWord;
    Button btnEnterWord;
    ListView lvWords;
    int i = 0;
    int check=1;
    int words;
    int wordOnplayer;
    int memberNum = 1;
    int comandNum = 0;
    int endCycle;
    int count = 0;
    int lvCount = 0;
    int bufposition=0;
    Intent intent;
    ArrayList<Comands> comands;
    ArrayAdapter<String> adapterW;
    SwipeDismissList mSwipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        if (getIntent().getBooleanExtra("finish", false)) this.finish();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        intent = getIntent();
        comands = getIntent().getParcelableArrayListExtra("mass");
        words = Integer.parseInt(intent.getStringExtra("words"));
        endCycle = 2*comands.size()*words;
        wordOnplayer=words;
        tvNameMember = (TextView) findViewById(R.id.tvNameMember);
        etWord = (EditText) findViewById(R.id.etWord);
        btnEnterWord = (Button) findViewById(R.id.btnEnterWord);
        btnEnterWord.setOnClickListener(this);
        tvNameMember.setText((CharSequence) comands.get(comandNum).getFistName());
        lvWords = (ListView) findViewById(R.id.lvWords);
        adapterW = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buf);
        lvWords.setAdapter(adapterW);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(15);
        etWord.setFilters(fa);

        mSwipeList = new SwipeDismissList(lvWords, new SwipeDismissList.OnDismissCallback() {
            public SwipeDismissList.Undoable onDismiss(AbsListView listView, final int position)  {
                final String item = buf.get(position);
                buf.remove(position);
                Temp.remove((wordOnplayer*bufposition)+position);
                lvCount--;
                count--;
                i--;
                setTitle("Ввод слов " + Integer.toString(count)+" из "+Integer.toString(wordOnplayer));
                adapterW.notifyDataSetChanged();
                return new SwipeDismissList.Undoable() {
                    @Override
                    public String getTitle() {
                        return "Слово " + item + " удалено";
                    }
                    @Override
                    public void undo() {
                        buf.add(position, item);
                        Temp.add((wordOnplayer * bufposition) + position,item);
                        lvCount++;
                        count++;
                        i++;
                        setTitle("Ввод слов " + Integer.toString(count)+" из "+Integer.toString(wordOnplayer));
                        adapterW.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_three, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnterWord:
                if(count==0){
                    Toast.makeText(this, "Чтобы удалить слово смахните его.", Toast.LENGTH_LONG).show();
                }
                if(!etWord.getText().toString().equals("")) {  //если ничего не введено
                    if (i == endCycle - 1) {
                        Temp.add(i, etWord.getText().toString());
                        buf.add(lvCount++, etWord.getText().toString());
                        adapterW.notifyDataSetChanged();
                        setTitle("Ввод слов " + Integer.toString(endCycle) + " из " + Integer.toString(endCycle));
                        AlertDialog.Builder builder = new AlertDialog.Builder(ThreeActivity.this);
                        builder.setTitle("Все готово")
                                .setMessage("Все слова введены, можете начинать первый этап")
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                int comN =comands.size();
                                                Intent intent = new Intent(ThreeActivity.this, Four2.class);
                                                intent.putExtra("mass", comands);
                                                intent.putExtra("words", Temp);
                                                intent.putExtra("numWords",endCycle);
                                                intent.putExtra("comNumb",comN);
                                                startActivity(intent);
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        btnEnterWord.setEnabled(false);
                        etWord.setText("");
                        etWord.setEnabled(false);
                    } else {
                        if (i == words - 1) {
                            if (memberNum % 2 == 0) {
                                add();
                                if(check==1) {
                                    setTitle("Ввод слов " + Integer.toString(count) + " из " + Integer.toString(wordOnplayer));
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ThreeActivity.this);
                                    builder.setTitle("Следующий игрок")
                                            .setMessage("Теперь игрок " + comands.get(comandNum).getFistName() + " вводит слова")
                                            .setCancelable(false)
                                            .setNegativeButton("ОК",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            buf.clear();
                                                            bufposition++;
                                                            lvCount = 0;
                                                            adapterW.notifyDataSetChanged();
                                                            count = 0;
                                                            setTitle("Ввод слов " + Integer.toString(count) + " из " + Integer.toString(wordOnplayer));
                                                            dialog.cancel();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    setTitle("Ввод слов " + Integer.toString(count) + " из " + Integer.toString(wordOnplayer));
                                    count = 0;
                                    tvNameMember.setText(comands.get(comandNum).getFistName());
                                    memberNum++;
                                    etWord.setText("");
                                    words += Integer.parseInt(intent.getStringExtra("words"));
                                }
                            } else {
                                add();
                                if(check==1) {
                                    setTitle("Ввод слов " + Integer.toString(count) + " из " + Integer.toString(wordOnplayer));
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ThreeActivity.this);
                                    builder.setTitle("Следующий игрок")
                                            .setMessage("Теперь игрок " + comands.get(comandNum).getSecondName() + " вводит слова")
                                            .setCancelable(false)
                                            .setNegativeButton("ОК",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            buf.clear();
                                                            bufposition++;
                                                            lvCount = 0;
                                                            adapterW.notifyDataSetChanged();
                                                            count = 0;
                                                            setTitle("Ввод слов " + Integer.toString(count) + " из " + Integer.toString(wordOnplayer));
                                                            dialog.cancel();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    setTitle("Ввод слов " + Integer.toString(count) + " из " + Integer.toString(wordOnplayer));
                                    count = 0;
                                    tvNameMember.setText(comands.get(comandNum).getSecondName());
                                    memberNum++;
                                    comandNum++;
                                    etWord.setText("");
                                    words += Integer.parseInt(intent.getStringExtra("words"));
                                }
                            }

                        } else {
                            add();
                        }
                    }
                }else{
                    Toast.makeText(this, "Ничего не введено, повторите ввод", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void add(){
        check=1;
        for (int j = 0; j < i; j++){
            if (Temp.get(j).equals(etWord.getText().toString())) {
                check*=0;
                break;
            }
        }
        if(check==1) {
            Temp.add(i, etWord.getText().toString());
            buf.add(lvCount++, etWord.getText().toString());
            adapterW.notifyDataSetChanged();
            setTitle("Ввод слов " + Integer.toString(++count) + " из " + Integer.toString(wordOnplayer));
            etWord.setText("");
            i++;
        }else{
            AlertDialog.Builder difWord = new AlertDialog.Builder(ThreeActivity.this);
            difWord.setTitle("Такое слово уже введено")
                    .setMessage("Введите другое слово")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = difWord.create();
            alert.show();
        }
    }
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Нажмите ещё раз чтобы вернуться",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

}
