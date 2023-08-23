package com.example.quizapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;


public class JoinQuiz extends AppCompatActivity {
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_session);

        TextView quiz_id =(TextView) findViewById(R.id.quizNameInput);
        Button joinQuiz = (Button) findViewById(R.id.joinQuizBtn);

        joinQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quizId = quiz_id.getText().toString();
                int student_id = getIntent().getIntExtra("Student",1);
                int quiz_id_int = Integer.parseInt(quizId);
                Intent intent = new Intent(JoinQuiz.this, QuizActivity.class);
                intent.putExtra("quizId", quiz_id_int);
                intent.putExtra("Student", student_id);
                startActivity(intent);

            }
        });
    }



}