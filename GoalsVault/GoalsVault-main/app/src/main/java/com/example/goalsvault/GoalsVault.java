package com.example.goalsvault;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GoalsVault extends AppCompatActivity {
    //Random Tips will be generated using this array tips[]
    static String tips[] = {"You will be exactly as happy as you decide to be!",
            "A grateful heart is a magnet to miracles", "Take time to make your soul happy",
            "Don't Let the bad days make you think you have a bad life"
            , "You Rock!", "You are resilient and you will get through it!", "Storms don't last forever!", "Focus on growth rather than perfection", "If you get tired, Learn to rest not to quit!", "Be the best version of yourself"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Orientation of Main Screen is set To Portrait
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView19);
        textView.setText(showTip()); //Text View will show tip returned randomly from an array
    }

    public void openChallenges(View view) { //OnClick method of Challenges Activity
        Intent intent = new Intent(this, Challenges.class);
        startActivity(intent);
    }

    public void openDoIt(View view) { //OnClick method of DoIt Activity
        Intent intent = new Intent(this, DoIt.class);
        startActivity(intent);
    }

    public void openGame(View view) { //OnClick method of Game
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    public String showTip() { //Method to return tip with Salutation

        int num = new Random().nextInt(tips.length);
        return "Hi! " + tips[num];
    }
}