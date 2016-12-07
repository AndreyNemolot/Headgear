package com.rmobile.Headgear;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrei on 02.09.2015.
 */

public class Comands implements Parcelable {
    private String[] names = new String[2];
    private int score;
    private String commandName;
    private int id;

    Comands(String[] names_, int number) {
        id = number;
        String[] comName = {"Зайчики", "Черепашки", "Лисички", "Тюленчики", "Коалы", "Тигрята", "Жирафики", "Утки", "Пингвинчики", "Панды", "Львята", "Кенгурята", "Волчата", "Обезьянки", "DreamTeam"};
        commandName = comName[id];
        for (int i = 0; i < names_.length; i++) names[i] = names_[i];
        score = 0;
    }

    public Comands(Parcel in) {
        Bundle bundle = in.readBundle();

        names = bundle.getStringArray("names");
        score = bundle.getInt("score");
        commandName = bundle.getString("commandName");
        id = bundle.getInt("id");
    }

    public void addScore(int scores) {
        score += scores;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public String getFistName() { return names[0]; }

    public  String getSecondName() { return names[1]; }

    @Override
    public String toString() {
        return commandName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("names", names);
        bundle.putInt("score", score);
        bundle.putInt("id", id);
        bundle.putString("commandName", commandName);

        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Comands createFromParcel(Parcel in) {
            return new Comands(in);
        }

        public Comands[] newArray(int size) {
            return new Comands[size];
        }
    };
}

