package com.example.irquizmcqsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DevelopersActivity extends AppCompatActivity {

    Button btnRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        btnRestart = (Button) findViewById(R.id.button4);
        Log.d("Ravi", "DevelopersActivity onCreate  Start 84");


        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ravi", "DevelopersActivity onClick  Start 85");
                Intent in2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in2);
                Log.d("Ravi", "DevelopersActivity onClick  Start 86");
            }
        });
    }
}