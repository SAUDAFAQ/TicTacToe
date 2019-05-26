package com.example.android.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserDetails_Activity extends AppCompatActivity {

    EditText name;
    Button submit;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_);

        getSupportActionBar().hide();

        name = (EditText) findViewById(R.id.username);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().length() < 1) {
                    Toast.makeText(UserDetails_Activity.this, "Please Enter You Name!", Toast.LENGTH_SHORT).show();
                } else {
                    username = name.getText().toString();
                    Intent intent = new Intent(UserDetails_Activity.this, MainActivity.class);
                    intent.putExtra("name", username);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
