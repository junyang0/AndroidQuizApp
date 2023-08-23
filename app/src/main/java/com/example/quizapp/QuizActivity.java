package com.example.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends Activity {
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button confirmNext;

    private TextView textViewQuestions;
    private TextView textViewScore;

    private int score=0;
    private TextView textViewQuestionCount;
    private TextView textViewCountdown;
    private TextView textViewCorrect, textViewWrong;

    private ArrayList<Questions> questionList;

    private int questionCounter;
    private int questionTotalCount;
    private Questions currentQuestions;
    private boolean answered;
    private int quiz_id;
    private int student_id;


    private Handler handler= new Handler();
    private ColorStateList buttonLabelColor;

    private int correctAnsCount =0;
    private int wrongAnsCount =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        setupUI();
        fetchDB(quiz_id);
        buttonLabelColor = rb1.getTextColors();

    }

    private void setupUI(){
        textViewCorrect = findViewById(R.id.txtCorrect);
        textViewWrong = findViewById(R.id.txtWrong);
        textViewCountdown = findViewById(R.id.txtViewTimer);
        textViewQuestionCount =findViewById(R.id.txtTotalQuestion);
        textViewScore = findViewById(R.id.txtScore);
        textViewQuestions = findViewById(R.id.textView);
        confirmNext = findViewById(R.id.button);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_Button1);
        rb2 = findViewById(R.id.radio_Button2);
        rb3 = findViewById(R.id.radio_Button3);
        rb4 = findViewById(R.id.radio_Button4);
        textViewCountdown.setText("10:00");
        quiz_id = getIntent().getIntExtra("quizId",1);
        student_id = getIntent().getIntExtra("Student",1);
    }

    private void fetchDB(int quiz_id){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionList= dbHelper.getAllQuestions(quiz_id);

        startQuiz();
    }

    private void addAnswer(int given_answer, int questionNumber){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insertAnswer(student_id, given_answer, questionNumber);
    }

    public class CountdownTimer {

        private Timer timer;
        private int seconds;

        public CountdownTimer(int seconds) {
            this.seconds = seconds;
        }

        public void start(String url) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (seconds == 0) {


                    } else {
                        System.out.println(getTimeLeft());
                        seconds--;
                    }
                }
            }, 0, 1000);
        }

        private String getTimeLeft() {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            return String.format("%02d:%02d", minutes, remainingSeconds);
        }
    }
    private void startQuiz() {
        questionTotalCount=questionList.size();
        Collections.shuffle(questionList);
        showQuestions();

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_Button1:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_checked));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        break;
                    case R.id.radio_Button2:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_checked));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        break;
                    case R.id.radio_Button3:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_checked));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        break;
                    case R.id.radio_Button4:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_checked));
                        break;



                }
            }
        });

        confirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answered){
                    if(rb1.isChecked() || rb2.isChecked()||rb3.isChecked()||rb4.isChecked()){
                        quizOperations();
                    }else{
                        Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                textViewCountdown.setText(timeLeftFormatted);
            }

            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time's Up! Next Question", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuizActivity.this, JoinQuiz.class);
                intent.putExtra("quizId", quiz_id);
                intent.putExtra("Student", student_id);
                startActivity(intent);

            }
        };

        timer.start();

    }

    private void quizOperations(){
        answered = true;
        RadioButton rbselected =findViewById(rbGroup.getCheckedRadioButtonId());
        int given_answer = rbGroup.indexOfChild(rbselected)+1;
        checkSolution(given_answer, rbselected);

    }

    private void checkSolution(int given_answer, RadioButton rbselected){
        if (given_answer == -1 || rbselected == null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showQuestions();
                }
            },500);
            return;
        }
        switch (currentQuestions.getAnswer()){
            case 1:
                if(currentQuestions.getAnswer()== given_answer ){
                    rb1.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_correct));
                    correctAnsCount++;
                    score+=10;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewCorrect.setText("Correct: "+String.valueOf(correctAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }else{
                    rbselected.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_wrong));
                    wrongAnsCount++;
                    score-=5;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewWrong.setText("Wrong: "+String.valueOf(wrongAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }
                break;
            case 2:
                if(currentQuestions.getAnswer()== given_answer){
                    rb2.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_correct));
                    correctAnsCount++;
                    score+=10;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewCorrect.setText("Correct: "+String.valueOf(correctAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }else{
                    rbselected.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_wrong));
                    wrongAnsCount++;
                    score-=5;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewWrong.setText("Wrong: "+String.valueOf(wrongAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }
                break;
            case 3:
                if(currentQuestions.getAnswer()== given_answer){
                    rb3.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_correct));
                    correctAnsCount++;
                    score+=10;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewCorrect.setText("Correct: "+String.valueOf(correctAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }else{
                    rbselected.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_wrong));
                    wrongAnsCount++;
                    score-=5;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewWrong.setText("Wrong: "+String.valueOf(wrongAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }
                break;
            case 4:
                if(currentQuestions.getAnswer()== given_answer){
                    rb4.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_correct));
                    correctAnsCount++;
                    score+=10;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewCorrect.setText("Correct: "+String.valueOf(correctAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }else{
                    rbselected.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_wrong));
                    wrongAnsCount++;
                    score-=5;
                    textViewScore.setText("Score: "+String.valueOf(score));
                    textViewWrong.setText("Wrong: "+String.valueOf(wrongAnsCount));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },500);
                }
                break;
        }

        if(questionCounter== questionTotalCount){
            confirmNext.setText("Confirm and Finish");
        }

        addAnswer(given_answer, currentQuestions.getQuestion_id());
    }

    private void wrong_answer(RadioButton rbselected){
        rbselected.setBackground(ContextCompat.getDrawable(this,R.drawable.ans_wrong));
    }

    private void showQuestions(){
        rbGroup.clearCheck();

        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));

        if (questionCounter<questionTotalCount){
            currentQuestions=questionList.get(questionCounter);
            textViewQuestions.setText(currentQuestions.getQuestion());
            rb1.setText(currentQuestions.getOption1());
            rb2.setText(currentQuestions.getOption2());
            rb3.setText(currentQuestions.getOption3());
            rb4.setText(currentQuestions.getOption4());

            questionCounter++;
            answered= false;
            confirmNext.setText("Confirm");

            textViewQuestionCount.setText("Questions: "+questionCounter+"/"+questionTotalCount);
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), scorepage.class);
                    intent.putExtra("score", score);
                    startActivity(intent);

                }
            },500);
        }
    }
}
