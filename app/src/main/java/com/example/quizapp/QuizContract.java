package com.example.quizapp;

import android.provider.BaseColumns;
// A contract class to standardize the column names of the question table.
public final class QuizContract {

    public QuizContract(){}

    public static class QuestionTable implements BaseColumns{

        public static final String TABLE_NAME = "quiz_questions";

        public static final String COLUMN_QUIZ_ID = "quiz_id";
        public static final String COLUMN_QUESTION = "question_text";
        public static final String OPTION_1 = "option1";
        public static final String OPTION_2 = "option2";
        public static final String OPTION_3 = "option3";
        public static final String OPTION_4 = "option4";

        public static final String COLUMN_ANSWER = "answer";



    }
}
