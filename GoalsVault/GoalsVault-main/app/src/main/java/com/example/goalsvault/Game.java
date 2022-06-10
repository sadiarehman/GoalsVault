package com.example.goalsvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Button button = findViewById(R.id.coinCheck);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref=getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                long coins=pref.getLong("vault",0);

                if (coins > 10) {

                    editor.putLong("vault",coins-=10).apply();
                    startGame();
                }
                else {
                    Toast.makeText(Game.this,"You don't have enough coins",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void startGame(){
        Intent intent = new Intent(this, GameEngine.class);
        startActivity(intent);
    }
}