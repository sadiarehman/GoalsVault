package com.example.goalsvault;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;

public class Challenges extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Setting orientation to Portrait
    }

    public void show_coins(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        long all_coins = pref.getLong("vault", 0); //Value of Vault will be displayed
        String str = Long.toString(all_coins);
        Button but = findViewById(R.id.button);
        but.setText(str); //To Show coins in the main Challenge Category Page
    }

    public void openStudy(View view) { //OnClick method of Study.class
        Intent intent = new Intent(this, Study.class);
        startActivity(intent);
    }

    public void openKindness(View view) { //OnClick method of Kindness.class
        Intent intent = new Intent(this, Kindness.class);
        startActivity(intent);
    }

    public void openConfidence(View view) { //OnClick method of Confidence.class
        Intent intent = new Intent(this, Confidence.class);
        startActivity(intent);
    }

    public void openSelfcare(View view) {  //OnClick method of SelfCare.class
        Intent intent = new Intent(this, SelfCare.class);
        startActivity(intent);
    }

    public void openMentalHealth(View view) {  //OnClick method of MentalHealth.class
        Intent intent = new Intent(this, MentalHealth.class);
        startActivity(intent);
    }

    public void openOptimism(View view) {  //OnClick method of Optimism.class
        Intent intent = new Intent(this, Optimism.class);
        startActivity(intent);
    }

}


