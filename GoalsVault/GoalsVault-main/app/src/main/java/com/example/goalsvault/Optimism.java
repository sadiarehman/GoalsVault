package com.example.goalsvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Optimism extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimism);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Orientation of the activity is set to Portrait mode
    }

    public void challenge_1(View view) { //On Click method of Button 1 (DAY 1)

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        Button text = findViewById(R.id.textView17);
        text.setText(display_coins1()); //Returned value from the display_coins1() method will be set as Text for Displaying Coins

        TextView textview = findViewById(R.id.textView1);
        textview.setText("Practice smiling .Smile at strangers,in the mirror and whenever you can."); //DAY 1 CHALLENGE
//textView1 is the TextVew which displays the challenges

//ALL check buttons except for 1A will be invisible
        CheckBox btn1 = findViewById(R.id.checkBox1A);
        btn1.setVisibility(View.VISIBLE);

        switch (pref.getString("A1", "a")) {
            case "T": {
                btn1.setChecked(true); //if the check button is checked then the button will be disabled too based on the value of shared pref value for the check button
                btn1.setEnabled(false);
                break;
            }
//if it is not checked then button will be enabled
            default: {
                btn1.setEnabled(true);
                break;

            }
        }

        CheckBox btn2 = findViewById(R.id.checkBox2A);
        btn2.setVisibility(View.INVISIBLE);

        CheckBox btn3 = findViewById(R.id.checkBox3A);
        btn3.setVisibility(View.INVISIBLE);

        CheckBox btn4 = findViewById(R.id.checkBox4A);
        btn4.setVisibility(View.INVISIBLE);

        CheckBox btn6 = findViewById(R.id.checkBox6A);
        btn6.setVisibility(View.INVISIBLE);

        CheckBox btn5 = findViewById(R.id.checkBox5A);
        btn5.setVisibility(View.INVISIBLE);


    }


    public void challenge_2(View view) {  //onClick Method of BUTTON 2 (Day 2)

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        //The date ( whenever the check button Day 1 completed is checked) is fetched.
        long milli_now = pref.getLong("category1", 0);
        Button text = findViewById(R.id.textView17);
        text.setText(display_coins1());//for displaying coins.
//Date will be recorded as soon as the button is pressed

        Calendar category_6 = Calendar.getInstance();
        long milli_secs_2 = category_6.getTimeInMillis();
        TextView textview = findViewById(R.id.textView1);


        CheckBox btn6 = findViewById(R.id.checkBox6A);
        btn6.setVisibility(View.INVISIBLE);
        CheckBox btn5 = findViewById(R.id.checkBox5A);
        btn5.setVisibility(View.INVISIBLE);
        CheckBox btn = findViewById(R.id.checkBox4A);
        btn.setVisibility(View.INVISIBLE);
        CheckBox btn1 = findViewById(R.id.checkBox1A);
        btn1.setVisibility(View.INVISIBLE);
        CheckBox btn3 = findViewById(R.id.checkBox3A);
        btn3.setVisibility(View.INVISIBLE);

        //values of stored date and current date will be compared, in case of no recorded date i.e 0, challenge will not be displayed
        if (milli_secs_2 - milli_now >= 86400000 & milli_now != 0) {

            textview.setText("Appreciate the little things today. Someone's smile ,the sunset,the sky.");

            CheckBox btn2 = findViewById(R.id.checkBox2A);
            btn2.setVisibility(View.VISIBLE);
//Only Check button 2A will be visible
            switch (pref.getString("A2", "a")) {

                case "T": {
                    btn2.setChecked(true);
                    btn2.setEnabled(false);
                    break;
                }

                default: {
                    btn2.setEnabled(true);
                    break;

                }
            }


        } else//A toast message will appear if the condition is not fulfilled
        {

            Toast.makeText(getApplicationContext(), "Come back on Day 2", Toast.LENGTH_SHORT).show();
            textview.setText("");
        }
    }

    //Same functionality for each button from challenge_2 to challenge_6 is implemented, here the state of each button is saved in separate shared preference
    public void challenge_3(View view) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        TextView textview = findViewById(R.id.textView1);
        long milli_now = pref.getLong("category1", 0);
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());

        Calendar day_3 = Calendar.getInstance();
        long milli_secs_3 = day_3.getTimeInMillis();


        CheckBox btn6 = findViewById(R.id.checkBox6A);
        btn6.setVisibility(View.INVISIBLE);
        CheckBox btn5 = findViewById(R.id.checkBox5A);
        btn5.setVisibility(View.INVISIBLE);
        CheckBox btn = findViewById(R.id.checkBox4A);
        btn.setVisibility(View.INVISIBLE);
        CheckBox btn1 = findViewById(R.id.checkBox1A);
        btn1.setVisibility(View.INVISIBLE);
        CheckBox btn2 = findViewById(R.id.checkBox2A);
        btn2.setVisibility(View.INVISIBLE);
        CheckBox btn3 = findViewById(R.id.checkBox3A);
        if (milli_secs_3 - milli_now >= 172800000 & milli_now != 0) {
            textview.setText("Take time to reflect on what you've achieved this year'");

            btn3.setVisibility(View.VISIBLE);
            switch (pref.getString("A3", "a")) {
                case "T": {
                    btn3.setChecked(true);
                    btn3.setEnabled(false);
                    break;
                }
                default: {
                    btn3.setEnabled(true);
                    break;

                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Come back on Day 3", Toast.LENGTH_SHORT).show();
            textview.setText("");
        }
    }


    public void challenge_4(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        long milli_now = pref.getLong("category1", 0);
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());

        Calendar day_4 = Calendar.getInstance();
        long milli_secs_4 = day_4.getTimeInMillis();


        TextView textview = findViewById(R.id.textView1);
        CheckBox btn6 = findViewById(R.id.checkBox6A);
        btn6.setVisibility(View.INVISIBLE);
        CheckBox btn5 = findViewById(R.id.checkBox5A);
        btn5.setVisibility(View.INVISIBLE);
        CheckBox btn4 = findViewById(R.id.checkBox4A);
        CheckBox btn1 = findViewById(R.id.checkBox1A);
        btn1.setVisibility(View.INVISIBLE);
        CheckBox btn2 = findViewById(R.id.checkBox2A);
        btn2.setVisibility(View.INVISIBLE);
        CheckBox btn3 = findViewById(R.id.checkBox3A);
        btn3.setVisibility(View.INVISIBLE);
        if (milli_secs_4 - milli_now >= 259200000 & milli_now != 0) {
            textview.setText("Rather then thinking of horrible things that might happen,consider the great things that could come from current situation.");

            btn4.setVisibility(View.VISIBLE);
            switch (pref.getString("A4", "a")) {
                case "T": {
                    btn4.setChecked(true);
                    btn4.setEnabled(false);
                    break;
                }
                default: {
                    btn4.setEnabled(true);
                    break;

                }
            }


        } else {
            Toast.makeText(getApplicationContext(), "Come back on Day 4", Toast.LENGTH_SHORT).show();
            textview.setText("");
        }
    }


    public void challenge_5(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        long milli_now = pref.getLong("category1", 0);
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());

        TextView textview = findViewById(R.id.textView1);

        Calendar day_5 = Calendar.getInstance();
        long milli_secs_5 = day_5.getTimeInMillis();


        CheckBox btn6 = findViewById(R.id.checkBox6A);
        btn6.setVisibility(View.INVISIBLE);
        CheckBox btn = findViewById(R.id.checkBox4A);
        btn.setVisibility(View.INVISIBLE);
        CheckBox btn1 = findViewById(R.id.checkBox1A);
        btn1.setVisibility(View.INVISIBLE);
        CheckBox btn2 = findViewById(R.id.checkBox2A);
        btn2.setVisibility(View.INVISIBLE);
        CheckBox btn3 = findViewById(R.id.checkBox3A);
        btn3.setVisibility(View.INVISIBLE);
        if (milli_secs_5 - milli_now >= 345600000 & milli_now != 0) {


            CheckBox btn5 = findViewById(R.id.checkBox5A);
            btn5.setVisibility(View.VISIBLE);
            switch (pref.getString("A5", "a")) {
                case "T": {
                    btn5.setChecked(true);
                    btn5.setEnabled(false);
                    break;
                }
                default: {
                    btn5.setEnabled(true);
                    break;

                }
            }


            textview.setText("Look for the good in every single person you see today.");

        } else {

            Toast.makeText(getApplicationContext(), "Come back on Day 5", Toast.LENGTH_SHORT).show();
            textview.setText("");
        }
    }

    public void challenge_6(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        long milli_now = pref.getLong("category1", 0);
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());

        Calendar day_6 = Calendar.getInstance();
        long milli_secs_6 = day_6.getTimeInMillis();
        TextView textview = findViewById(R.id.textView1);


        CheckBox btn5 = findViewById(R.id.checkBox5A);
        btn5.setVisibility(View.INVISIBLE);
        CheckBox btn = findViewById(R.id.checkBox4A);
        btn.setVisibility(View.INVISIBLE);
        CheckBox btn1 = findViewById(R.id.checkBox1A);
        btn1.setVisibility(View.INVISIBLE);
        CheckBox btn2 = findViewById(R.id.checkBox2A);
        btn2.setVisibility(View.INVISIBLE);
        CheckBox btn3 = findViewById(R.id.checkBox3A);
        btn3.setVisibility(View.INVISIBLE);
        if (milli_secs_6 - milli_now >= 432000000 & milli_now != 0) {

            textview.setText("Stop caring for what other people think.The thoughts of other people are not designed to make you happy. ");
            CheckBox btn6 = findViewById(R.id.checkBox6A);
            btn6.setVisibility(View.VISIBLE);
            switch (pref.getString("A6", "a")) {
                case "T": {
                    btn6.setChecked(true);
                    btn6.setEnabled(false);
                    break;
                }
                default: {
                    btn6.setEnabled(true);
                    break;

                }
            }

        } else {
            Toast.makeText(getApplicationContext(), "Come back on Day 6", Toast.LENGTH_SHORT).show();
            textview.setText("");
        }

    }

    //Check buttons are used to record whether challenge is completed or not.
    //onClick method for check button 1
    public void challenge_1check(View view) {
//Day 2 to 6 will depend on the disability of check button 1
        //Once it is disabled, it can't be enabled again
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Calendar category_6 = Calendar.getInstance();
        //Date will be stored as soon as the check button is pressed
        editor.putLong("category1", category_6.getTimeInMillis());
        //vault will be added with 10 coins as soon as the challenge is completed
        long l = pref.getLong("vault", 0) + 10;
        editor.putLong("vault", l).apply();
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());      //coins will be automatically displayed
        editor.putString("A1", "T").apply();
        CheckBox btn1 = findViewById(R.id.checkBox1A);

        btn1.setEnabled(false);


    }

//Again button will be disabled once it is pressed after visibility. And coins will be added for each
    //Challenge_2check to challenge_6check hve the same functionality, and different key value pairs of preferences to disable buttons

    public void challenge_2check(View view) {
        //Day 2 to 6 will depend on the disability of check button 1
        //Once it is disabled, it can't be enabled again
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //vault will be added with 10 coins as soon as the challenge is completed
        long l = pref.getLong("vault", 0) + 10;
        editor.putLong("vault", l).apply();
        Button but = findViewById(R.id.textView17);
        //coins will be automatically displayed
        but.setText(display_coins1());
        editor.putString("A2", "T").apply();
        CheckBox btn2 = findViewById(R.id.checkBox2A);

        btn2.setEnabled(false);


    }

    public void challenge_3check(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        long l = pref.getLong("vault", 0) + 10;
        editor.putLong("vault", l).apply();
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());

        editor.putString("A3", "T").apply();
        CheckBox btn1 = findViewById(R.id.checkBox3A);

        btn1.setEnabled(false);


    }

    public void challenge_4check(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        long l = pref.getLong("vault", 0) + 10;
        editor.putLong("vault", l).apply();
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());

        editor.putString("A4", "T").apply();
        CheckBox btn1 = findViewById(R.id.checkBox4A);

        btn1.setEnabled(false);


    }

    public void challenge_5check(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        long l = pref.getLong("vault", 0) + 10;
        editor.putLong("vault", l).apply();
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());
        editor.putString("A5", "T").apply();
        CheckBox btn1 = findViewById(R.id.checkBox5A);

        btn1.setEnabled(false);
    }

    public void challenge_6check(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        long l = pref.getLong("vault", 0) + 10;
        editor.putLong("vault", l).apply();
        Button but = findViewById(R.id.textView17);
        but.setText(display_coins1());
        editor.putString("A6", "T").apply();
        CheckBox btn1 = findViewById(R.id.checkBox6A);

        btn1.setEnabled(false);

    }

    //display_coins1 will return a string value which will show coins value
    public String display_coins1() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        long l = pref.getLong("vault", 0);
        return Long.toString(l);

    }

}







