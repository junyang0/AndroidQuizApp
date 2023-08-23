package com.example.quizapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class scorepage extends AppCompatActivity {
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_page);

        TextView scoreText =(TextView) findViewById(R.id.scoretxt);
        Button newSession = (Button) findViewById(R.id.JoinNewSession);

        score = getIntent().getIntExtra("score",1);
        scoreText.setText(String.valueOf(score));

        newSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(scorepage.this, JoinQuiz.class);
                startActivity(intent);

            }
        });
    }



}