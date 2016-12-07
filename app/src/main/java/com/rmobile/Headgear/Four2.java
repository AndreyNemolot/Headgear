package com.rmobile.Headgear;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Four2 extends ActionBarActivity implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

    TextView tvTimer;
    TextView tvTeamName;
    TextView tvPlayerName;
    TextView tvFAQ;
    Button btnWords;
    MyTimer timer;
    Intent intent;
    ArrayList<Comands> comand;
    ArrayList<String> Player;
    ArrayList<String> Temp = new ArrayList<String>(0);
    ArrayList<String> Words = new ArrayList<String>(0);
    int tempScore;
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    SoundPool sp;
    int soundIdGudok;
    ListView lv_navigation_drawer;
    SimpleAdapter sAdapter;
    SimpleAdapter sAdapterHead;
    boolean flag=false;
    int playerCounter=0,comCounter=0,counterW=0, Stage=1;
    int end,comNumb;
    FloatingActionButton fab;
    final String ATTRIBUTE_NAME_TEAM = "team";
    final String ATTRIBUTE_NAME_SCORE = "score";
    final String ATTRIBUTE_NAME_TEAM_HEAD = "team";
    final String ATTRIBUTE_NAME_SCORE_HEAD = "score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("finish", false)) finish();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four2);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        soundIdGudok = sp.load(this, R.raw.gudok, 1);

        tvFAQ = (TextView) findViewById(R.id.tvFAQ);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvTeamName = (TextView) findViewById(R.id.tvTeamName);
        tvPlayerName = (TextView) findViewById(R.id.tvPlayerName);
        btnWords = (Button)findViewById(R.id.btnWords);
        btnWords.setTranslationX(0);
        intent = getIntent();
        comand = getIntent().getParcelableArrayListExtra("mass");
        Player = getIntent().getStringArrayListExtra("Key");
        Words = getIntent().getStringArrayListExtra("words");
        end = getIntent().getIntExtra("numWords", 0);
        comNumb = getIntent().getIntExtra("comNumb", 0);
        Temp=Words;
        setTitle("Этап "+Integer.toString(Stage) + " - Объясни");
        tvFAQ.setVisibility(View.INVISIBLE);
        tvTeamName.setText(comand.get(comCounter).toString());
        tvPlayerName.setText("Загадывает: "+comand.get(playerCounter).getFistName());
        timer = new MyTimer(30000, 1000);
        timer.onTick(30000);
        mixString(Temp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flag) {
                    timer.start(); //Таймер пошел
                    tempScore=comand.get(comCounter).getScore();
                    btnWords.setText(Temp.get(counterW)); //Текст кнопки - текущее слово
                    if (counterW==end) {    //если кончились слова
                        AlertDialog.Builder info = new AlertDialog.Builder(Four2.this);
                        info.setTitle(comand.get(comCounter).toString())
                                .setMessage("набрала в этом раунде "+(comand.get(comCounter).getScore()-tempScore)+" очка(ов)")
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Four2.this);
                                                builder.setTitle("Конец " +Integer.toString(Stage-1)+ " этапа")
                                                        .setMessage("Слова закончились, пора подвести результаты")
                                                        .setCancelable(false)
                                                        .setNegativeButton("ОК",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        btnWords.setText("Слова");
                                                                        dialog.cancel();
                                                                    }
                                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();

                                            }
                                        });
                        AlertDialog alertInf = info.create();
                        alertInf.show();
                        timer.cancel();
                    }
                    tvTimer.setTextColor(Color.parseColor("#02887b"));
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_white_24dp));
                    tvFAQ.setVisibility(View.VISIBLE);
                    flag=true;
                } else {
                    if(flag) {                    //нажатие закончить
                        AlertDialog.Builder builder = new AlertDialog.Builder(Four2.this);
                        builder.setTitle("Закончить?")
                                .setMessage("Уверены что хотите закончить?")
                                .setCancelable(false)
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        timer.cancel();
                                        AlertDialog.Builder info2 = new AlertDialog.Builder(Four2.this);
                                        info2.setTitle(comand.get(comCounter).toString())
                                                .setMessage("набрала в этом раунде "+(comand.get(comCounter).getScore()-tempScore)+" очка(ов)")
                                                .setCancelable(false)
                                                .setNegativeButton("ОК",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                btnWords.setText("Слова");
                                                                dialog.cancel();
                                                            }
                                                        });
                                        AlertDialog alertInf2 = info2.create();
                                        alertInf2.show();
                                        if (Stage == 3) {
                                            timer = new MyTimer(20000, 1000);
                                            timer.onTick(20000);
                                        } else {
                                            timer = new MyTimer(30000, 1000);
                                            timer.onTick(30000);
                                        }
                                        String Buf = Temp.get(counterW);
                                        int r = counterW + (int) (Math.random() * (((Temp.size() - 1) - counterW) + 1));
                                        Temp.set(counterW,Temp.get(r));
                                        Temp.set(r,Buf);
                                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                                        tvFAQ.setVisibility(View.INVISIBLE);
                                        tvTimer.setTextColor(Color.parseColor("#656565"));

                                        result();
                                        flag = false;
                                        if((playerCounter) == (comand.size()*2) - 1) {
                                            comCounter=0;
                                            tvTeamName.setText(comand.get(comCounter).toString());
                                            tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getFistName());
                                            playerCounter=0;
                                        }else {
                                            if (playerCounter < comand.size() - 1) {
                                                comCounter++;
                                                tvTeamName.setText(comand.get(comCounter).toString());
                                                tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getFistName());
                                                playerCounter++;
                                            } else {
                                                if (playerCounter == comand.size() - 1) {
                                                    comCounter = 0;
                                                    tvTeamName.setText(comand.get(comCounter).toString());
                                                    tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getSecondName());
                                                    playerCounter++;
                                                } else {
                                                    if (playerCounter >= comand.size()) {
                                                        comCounter++;
                                                        tvTeamName.setText(comand.get(comCounter).toString());
                                                        tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getSecondName());
                                                        playerCounter++;
                                                    }
                                                }
                                            }
                                        }
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Нет",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        result();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        btnWords.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if (flag == true) {
                    if (counterW < end - 1) {
                        comand.get(comCounter).addScore(1);
                        counterW++;
                        TranslateAnimation animation = new TranslateAnimation(0, (float) btnWords.getWidth(), 0f, 0f);
                        animation.setDuration(350);
                        animation.setFillBefore(true);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                btnWords.animate().alpha(0.0f);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                btnWords.animate().alpha(1.0f);
                                btnWords.setText(Temp.get(counterW));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        btnWords.startAnimation(animation);
                    } else {
                        comand.get(comCounter).addScore(1);
                        Stage++;
                        timer.cancel();
                        AlertDialog.Builder info = new AlertDialog.Builder(Four2.this);
                        info.setTitle(comand.get(comCounter).toString())
                                .setMessage("набрала в этом раунде " + (comand.get(comCounter).getScore() - tempScore) + " очка(ов)")
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                btnWords.setText("Слова");
                                                dialog.cancel();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Four2.this);
                                                builder.setTitle("Конец " +Integer.toString(Stage-1)+ " этапа")
                                                        .setMessage("Слова закончились, пора подвести результаты")
                                                        .setCancelable(false)
                                                        .setNegativeButton("ОК",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        Intent intent = new Intent(Four2.this, FiveActivity.class);
                                                                        intent.putExtra("comands", comand);
                                                                        intent.putExtra("Stage", Stage);
                                                                        startActivityForResult(intent, 1);
                                                                        dialog.cancel();
                                                                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                                                                        tvFAQ.setVisibility(View.INVISIBLE);
                                                                        result();
                                                                        playerCounter = 0;
                                                                        comCounter = 0;
                                                                        counterW = 0;
                                                                        flag = false;
                                                                        tvTeamName.setText(comand.get(comCounter).toString());
                                                                        tvPlayerName.setText("Загадывает: "+comand.get(playerCounter).getFistName());
                                                                        if (Stage == 3) {
                                                                            setTitle("Этап " +Integer.toString(Stage)+ " - Одно слово");
                                                                            timer = new MyTimer(20000, 1000);
                                                                            timer.onTick(20000);
                                                                        } else {
                                                                            setTitle("Этап " +Integer.toString(Stage)+ " - Покажи");
                                                                            timer = new MyTimer(30000, 1000);
                                                                            timer.onTick(30000);
                                                                        }
                                                                        mixString(Temp);
                                                                    }
                                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                        });
                        AlertDialog alertInf = info.create();
                        alertInf.show();
                    }
                }
            }

            public void onSwipeLeft() {
                if (flag) {
                    if (counterW < end - 1) {
                        comand.get(comCounter).addScore(1);
                        counterW++;
                        TranslateAnimation animation = new TranslateAnimation(0, (float) -btnWords.getWidth(), 0f, 0f);
                        animation.setDuration(300);
                        animation.setFillBefore(true);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                btnWords.animate().alpha(0.0f);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                btnWords.animate().alpha(1.0f);
                                btnWords.setText(Temp.get(counterW));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        btnWords.startAnimation(animation);

                    } else {
                        comand.get(comCounter).addScore(1);
                        Stage++;
                        timer.cancel();
                        AlertDialog.Builder info = new AlertDialog.Builder(Four2.this);
                        info.setTitle(comand.get(comCounter).toString())
                                .setMessage("набрала в этом раунде " + (comand.get(comCounter).getScore() - tempScore) + " очка(ов)")
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                btnWords.setText("Слова");
                                                dialog.cancel();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Four2.this);
                                                builder.setTitle("Конец " +Integer.toString(Stage-1)+ " этапа")
                                                        .setMessage("Слова закончились, пора подвести результаты")
                                                        .setCancelable(false)
                                                        .setNegativeButton("ОК",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        Intent intent = new Intent(Four2.this, FiveActivity.class);
                                                                        intent.putExtra("comands", comand);
                                                                        intent.putExtra("Stage", Stage);
                                                                        startActivityForResult(intent, 1);
                                                                        dialog.cancel();
                                                                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                                                                        tvFAQ.setVisibility(View.INVISIBLE);
                                                                        result();
                                                                        playerCounter = 0;
                                                                        comCounter = 0;
                                                                        counterW = 0;
                                                                        flag = false;
                                                                        tvTeamName.setText(comand.get(comCounter).toString());
                                                                        tvPlayerName.setText("Загадывает: "+comand.get(playerCounter).getFistName());
                                                                        if (Stage == 3) {
                                                                            setTitle("Этап " +Integer.toString(Stage)+ " - Одно слово");
                                                                            timer = new MyTimer(20000, 1000);
                                                                            timer.onTick(20000);
                                                                        } else {
                                                                            setTitle("Этап " +Integer.toString(Stage)+ " - Покажи");
                                                                            timer = new MyTimer(30000, 1000);
                                                                            timer.onTick(30000);
                                                                        }
                                                                        mixString(Temp);
                                                                    }
                                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                        });
                        AlertDialog alertInf = info.create();
                        alertInf.show();
                    }
                }
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    public void Stopgame(){
        AlertDialog.Builder info2 = new AlertDialog.Builder(Four2.this);
        info2.setTitle(comand.get(comCounter).toString())
                .setMessage("набрала в этом раунде "+(comand.get(comCounter).getScore()-tempScore)+" очка(ов)")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                btnWords.setText("Слова");
                                dialog.cancel();
                            }
                        });
        AlertDialog alertInf2 = info2.create();
        alertInf2.show();
        if((playerCounter) == (comand.size()*2) - 1) {
            playerCounter=0;
            comCounter=0;
            flag=false;
            timer.cancel();
            if (Stage == 3) {
                timer = new MyTimer(20000, 1000);
                timer.onTick(20000);
            }else {
                timer = new MyTimer(30000, 1000);
                timer.onTick(30000);
            }
            String Buf=Temp.get(counterW);
            int r = counterW + (int) (Math.random() * (((Temp.size()-1) - counterW) + 1));
            Temp.set(counterW,Temp.get(r));
            Temp.set(r,Buf);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
            tvFAQ.setVisibility(View.INVISIBLE);
            tvTimer.setTextColor(Color.parseColor("#656565"));
            result();
            tvTeamName.setText(comand.get(comCounter).toString());
            tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getFistName());
        }
        if(flag) {
            timer.cancel();
            if (Stage == 3) {
                timer = new MyTimer(20000, 1000);
                timer.onTick(20000);
            }else {
                timer = new MyTimer(30000, 1000);
                timer.onTick(30000);
            }
            String Buf=Temp.get(counterW);
            int r = counterW + (int) (Math.random() * (((Temp.size()-1) - counterW) + 1));
            Temp.set(counterW,Temp.get(r));
            Temp.set(r,Buf);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
            tvFAQ.setVisibility(View.INVISIBLE);
            tvTimer.setTextColor(Color.parseColor("#656565"));
            result();
            flag = false;
            if (playerCounter < comand.size() - 1) {
                comCounter++;
                tvTeamName.setText(comand.get(comCounter).toString());
                tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getFistName());
                playerCounter++;
            } else {
                if (playerCounter == comand.size() - 1) {
                    comCounter = 0;
                    tvTeamName.setText(comand.get(comCounter).toString());
                    tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getSecondName());
                    playerCounter++;
                } else {
                    if (playerCounter >= comand.size()) {
                        comCounter++;
                        tvTeamName.setText(comand.get(comCounter).toString());
                        tvPlayerName.setText("Загадывает: "+comand.get(comCounter).getSecondName());
                        playerCounter++;
                    }
                }
            }
        }
    }

    public void result(){
        lv_navigation_drawer = (ListView) findViewById(R.id.lv_navigation_drawer);
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(comand.size());
        ArrayList<Map<String, String>> data_head = new ArrayList<Map<String, String>>(1);
        Map<String, String> m;
        m = new HashMap<String, String>();
        m.put(ATTRIBUTE_NAME_TEAM_HEAD, "Команда");
        m.put(ATTRIBUTE_NAME_SCORE_HEAD, "Очки");
        data_head.add(m);
        String[] fromhead = {ATTRIBUTE_NAME_TEAM_HEAD, ATTRIBUTE_NAME_SCORE_HEAD};
        int[] tohead = {R.id.tvName, R.id.tvScore};
        sAdapterHead = new SimpleAdapter(this, data_head, R.layout.itemhead4, fromhead, tohead);
        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(sAdapterHead);

        for (int i = 0; i < comand.size(); i++) {
            m = new HashMap<String, String>();
            m.put(ATTRIBUTE_NAME_TEAM, comand.get(i).toString());
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
        String[] from = {ATTRIBUTE_NAME_SCORE, ATTRIBUTE_NAME_TEAM};
        int[] to = {R.id.tvScore, R.id.tvTeamName};
        sAdapter = new SimpleAdapter(this, data, R.layout.item4act, from, to);
        mergeAdapter.addAdapter(sAdapter);
        lv_navigation_drawer.setAdapter(mergeAdapter);
    }

    public void mixString(ArrayList<String> T){
        int size=T.size(),r;
        for(int n=0;n<size;n++){
            String Buf=Temp.get(n);
            r = n + (int)(Math.random() * (((size-1) - n) + 1));
            Temp.set(n,Temp.get(r));
            Temp.set(r,Buf);
        }
    }

    //private static long back_pressed;

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
    }

    public class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
            tvTimer.setText("");
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            Vibrator vibrator = (Vibrator) getSystemService (VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
            sp.play(soundIdGudok, 1, 1, 0, 0, 1);
            AlertDialog.Builder builder = new AlertDialog.Builder(Four2.this);
            builder.setTitle("Угадали?")
                    .setMessage("Слово угадано?")
                    .setCancelable(false)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (flag) {
                                if (counterW < end - 1) {
                                    comand.get(comCounter).addScore(1);
                                    counterW++;
                                    TranslateAnimation animation = new TranslateAnimation(0, (float) btnWords.getWidth(), 0f, 0f);
                                    animation.setDuration(350);
                                    animation.setFillBefore(true);
                                    animation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                            btnWords.animate().alpha(0.0f);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            btnWords.animate().alpha(1.0f);
                                            btnWords.setText(Temp.get(counterW));
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    btnWords.startAnimation(animation);
                                } else {
                                    comand.get(comCounter).addScore(1);
                                    Stage++;
                                    timer.cancel();
                                    btnWords.setText("Слова");
                                    dialog.cancel();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Four2.this);
                                    builder.setTitle("Конец " +Integer.toString(Stage-1)+ " этапа")
                                            .setMessage("Слова закончились, пора подвести результаты")
                                            .setCancelable(false)
                                            .setNegativeButton("ОК",
                                                    new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {Intent intent = new Intent(Four2.this, FiveActivity.class);
                                                                                    intent.putExtra("comands", comand);
                                                                                    intent.putExtra("Stage", Stage);
                                                                                    startActivityForResult(intent, 1);
                                                                                    dialog.cancel();
                                                                                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                                                                                    tvFAQ.setVisibility(View.INVISIBLE);
                                                                                    result();
                                                                                    playerCounter = 0;
                                                                                    comCounter = 0;
                                                                                    counterW = 0;
                                                                                    flag = false;
                                                                                    tvTeamName.setText(comand.get(comCounter).toString());
                                                                                    tvPlayerName.setText("Загадывает: "+comand.get(playerCounter).getFistName());
                                                                                    if (Stage == 3) {
                                                                                        setTitle("Этап " +Integer.toString(Stage)+ " - Одно слово");
                                                                                        timer = new MyTimer(20000, 1000);
                                                                                        timer.onTick(20000);
                                                                                    } else {
                                                                                        setTitle("Этап " +Integer.toString(Stage)+ " - Покажи");
                                                                                        timer = new MyTimer(30000, 1000);
                                                                                        timer.onTick(30000);
                                                                                    }
                                                                                    mixString(Temp);
                                                                                }
                                                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                }
                                }
                            Stopgame();
                        }
                    })
                    .setNegativeButton("Нет",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Stopgame();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();



        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            tvTimer.setText("Время:\t" + millisUntilFinished / 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_four, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_newgame:
                final Intent intent = new Intent(this, MainActivity.class);
                new AlertDialog.Builder(this)
                        .setTitle("Начать новую игру?")
                        .setMessage("Вы действительно хотите начать новую игру?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                //SomeActivity - имя класса Activity для которой переопределяем onBackPressed();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("finish", true);
                                startActivity(intent);
                            }
                        }).create().show();


               // startActivity(new Intent(this, MainActivity.class));

                break;
        }

        if (toggle != null && toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
       /* if (toggle != null && toggle.onOptionsItemSelected(item))
            return true;
        startActivity(intent);
        startActivity(new Intent(this, MainActivity.class));
                return super.onOptionsItemSelected(item);*/

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


}