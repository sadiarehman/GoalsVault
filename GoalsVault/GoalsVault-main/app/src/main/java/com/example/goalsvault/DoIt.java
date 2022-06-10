package com.example.goalsvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class DoIt extends AppCompatActivity {
    private ArrayList<String> tasks;
    private ArrayAdapter<String> tasksAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it);
        listView = findViewById(R.id.listView);
        FloatingActionButton floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        Toast.makeText(getApplicationContext(),"Long press on completed tasks to get rid of them. Happy productivity!",Toast.LENGTH_LONG).show();
        floatingActionButton2.setOnClickListener(this::addTask);

        tasks = new ArrayList<>();
        tasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        listView.setAdapter(tasksAdapter);


        setUpListViewListener();
        //method for deleting the tasks



    }

    private void setUpListViewListener() {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {    //when the user long presses on tasks
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences pref=getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);

                SharedPreferences.Editor editor=pref.edit();

                long coin=pref.getLong("vault",0)+5; //updates the number of coins

                editor.putLong("vault",coin).apply();
                Button points=findViewById(R.id.points);
                points.setText(show_coins());


                Toast.makeText(getApplicationContext(), "Task Accomplished!", Toast.LENGTH_SHORT).show();
                tasks.remove(position);

                Toast.makeText(getApplicationContext(),"5 points added",Toast.LENGTH_SHORT).show();
                tasksAdapter.notifyDataSetChanged();
                return true;
            }



        });


    }

    private void addTask(View view) {                 //method for adding new tasks
        EditText input = findViewById(R.id.editTask);
        String taskText = input.getText().toString(); //extract string input
        if (!(taskText.equals(""))) {   //check for empty strings
            tasksAdapter.add(taskText);
            input.setText("");

        } else {
            Toast.makeText(getApplicationContext(), "Enter text", Toast.LENGTH_SHORT).show();
        }


    }
    public String show_coins(){ //This method will return coins in String format
        SharedPreferences pref=getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        long all_coins=pref.getLong("vault",0);
        return Long.toString(all_coins);


    }





}




