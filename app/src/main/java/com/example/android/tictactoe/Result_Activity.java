package com.example.android.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Result_Activity extends AppCompatActivity {

    private String username;
    private String result, total_time;
    private int secs;
    private TextView result_heading, time_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_);

        username = getIntent().getStringExtra("name");
        result = getIntent().getStringExtra("result");
        total_time = getIntent().getStringExtra("time");
        result_heading = (TextView) findViewById(R.id.result_head);
        time_result = (TextView) findViewById(R.id.time);

        time_result.setText("Time of thinking " + total_time.toString() + " seconds");
        if (result.equals("Lost"))
            result_heading.setText("Sorry " + username + ", You " + result);
        else if (result.equals("Draw"))
            result_heading.setText("Sorry " + username + ", You " + result);
        else
            result_heading.setText("Congratulations" + username + ", You " + result);
        Button rstBtn = (Button) findViewById(R.id.btnRst);
        rstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Result_Activity.this, UserDetails_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        Button qutBtn = (Button) findViewById(R.id.btnQuit);
        qutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

    }
}
