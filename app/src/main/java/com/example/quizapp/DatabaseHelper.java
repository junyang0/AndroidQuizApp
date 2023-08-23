package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.quizapp.QuizContract.*;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quizApp.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+QuestionTable.TABLE_NAME);
        onCreate(db);
    }


    //To add
    private void addQuestions(Questions questions){
        ContentValues cv= new ContentValues();
        cv.put(QuestionTable.COLUMN_QUIZ_ID, questions.getQuiz_id());
        cv.put(QuestionTable.COLUMN_QUESTION, questions.getQuestion());
        cv.put(QuestionTable.OPTION_1, questions.getOption1());
        cv.put(QuestionTable.OPTION_2, questions.getOption2());
        cv.put(QuestionTable.OPTION_3, questions.getOption3());
        cv.put(QuestionTable.OPTION_4, questions.getOption4());
        cv.put(QuestionTable.COLUMN_ANSWER, questions.getAnswer());

        db.insert(QuestionTable.TABLE_NAME, null, cv);

    }



    private void fillQuestionTable(){
        Questions q1= new Questions(1, "Which Prof is the best prof in IEM?",
                "Prof Foo", "Prof Chua", "Prof Lee", "Prof Tan",
                2);
        addQuestions(q1);

        Questions q2= new Questions(1,"What is the course code for Intro to Design and Proj?",
                "IM2073", "EE2073", "IM1337", "IM4201",
                1);
        addQuestions(q2);

        Questions q3= new Questions(1,"What grade is this project going to get?",
                "A+", "A", "A-", "B+",
                1);
        addQuestions(q3);
        Questions q4= new Questions(1,"What is Junyang's GPA?",
                "5.0", "4.5", "4.75", "4.97",
                1);
        addQuestions(q4);
    }

    //Gets questions in the form of an array List from DB
    public ArrayList<Questions> getAllQuestions(int quiz_id){
        ArrayList<Questions> questionsList = new ArrayList<>();

        db = getReadableDatabase();
        String Projection[] ={
                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.OPTION_1,
                QuestionTable.OPTION_2,
                QuestionTable.OPTION_3,
                QuestionTable.OPTION_4,
                QuestionTable.COLUMN_ANSWER,
        };

        String[] quiz_number = new String[]{String.valueOf(quiz_id)};

        Cursor c = db.query(QuestionTable.TABLE_NAME,
                Projection,
                "quiz_id = ?",
                quiz_number,
                null,
                null,
                null
        );

        if (c.moveToFirst()){

            do {
                Questions questions =new Questions();
                questions.setQuestion_id(c.getInt(c.getColumnIndexOrThrow(QuestionTable._ID)));
                questions.setQuestion(c.getString(c.getColumnIndexOrThrow(QuestionTable.COLUMN_QUESTION)));
                questions.setOption1(c.getString(c.getColumnIndexOrThrow(QuestionTable.OPTION_1)));
                questions.setOption2(c.getString(c.getColumnIndexOrThrow(QuestionTable.OPTION_2)));
                questions.setOption3(c.getString(c.getColumnIndexOrThrow(QuestionTable.OPTION_3)));
                questions.setOption4(c.getString(c.getColumnIndexOrThrow(QuestionTable.OPTION_4)));
                questions.setAnswer(c.getInt(c.getColumnIndexOrThrow(QuestionTable.COLUMN_ANSWER)));

                questionsList.add(questions);
            }while (c.moveToNext());

        }
        c.close();

        return questionsList;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        String createStudentTableQuery = "CREATE TABLE students (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, password TEXT)";
        db.execSQL(createStudentTableQuery);
        String createTeacherTableQuery = "CREATE TABLE teachers (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, password TEXT)";
        db.execSQL(createTeacherTableQuery);
        String createTeachersQuizTableQuery = "CREATE TABLE teachersQuiz (id INTEGER, " +
                "questionAndAnswerID TEXT)";
        db.execSQL(createTeachersQuizTableQuery);
        String create_questions_table ="CREATE TABLE " + QuestionTable.TABLE_NAME + " ( " + QuestionTable._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                QuestionTable.COLUMN_QUIZ_ID + " INTEGER, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.OPTION_1 + " TEXT, " +
                QuestionTable.OPTION_2 + " TEXT, " +
                QuestionTable.OPTION_3 + " TEXT, " +
                QuestionTable.OPTION_4 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER + " INTEGER" +
                ")";
        db.execSQL(create_questions_table);
        String createAnswersQuery = "CREATE TABLE answers (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, "+
                "answer_val INTEGER, "+
                "question_num INTEGER "+

                ")";
        db.execSQL(createAnswersQuery);

        fillQuestionTable();
    }

    //Inserts student answers into table
    public void insertAnswer(int StudentID, int answer, int question_num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("student_id", StudentID);
        cv.put("answer_val", answer);
        cv.put("question_num", question_num);
        db.insert("answers", null, cv);

    }

    private void fillAnswerTable(){
        insertAnswer(1, 1, 1);
        insertAnswer(1, 2, 2);
        insertAnswer(1, 3, 3);

    }





    public void insertData(String userType, String username, String password) throws InvalidKeyException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert(userType, null, values);
        if (result == -1) {
            Log.d("Insertion Error", "Data insertion for user failed");
        } else {
            Log.d("Insertion Success", "Data inserted for user successfully");
        }
        if (userType.equals("teachers")){
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT id FROM teachers WHERE username = ?", new String[] { username });
            boolean exists = cursor.getCount() > 0;
            if (!exists){
                throw new InvalidKeyException("teacher does not exist");
            }
            else {
                if (cursor.moveToFirst()) {
                    int idOfTeacherFromDatabase = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    values = new ContentValues();
                    values.put("id", idOfTeacherFromDatabase);
                    result = db.insert("teachersQuiz" , null, values);
                }
            }

            result = db.insert(userType, null, values);
        }

        db.close();
    }


}