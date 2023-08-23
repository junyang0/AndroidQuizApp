package com.example.quizapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;



public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        db = dbHelper.getReadableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton loginButton = (MaterialButton) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            String usernameString, passwordString = "";
            @Override
            public void onClick(View v) {
                usernameString = username.getText().toString();
                passwordString = password.getText().toString();

                    if (checkUsernameInDB(usernameString)) {
                        //correct
                        if (authenticate(usernameString, passwordString) ==1) {
                            Toast.makeText(MainActivity.this, "STUDENT LOGIN-ED", Toast.LENGTH_SHORT).show();
                            Intent intentJoinQuiz = new Intent(MainActivity.this, JoinQuiz.class);// redirect at this point
                            Cursor cursor = db.rawQuery("SELECT id FROM students WHERE username = ?", new String[]{usernameString});
                            int id=1;
                            if (cursor.moveToFirst()) {
                                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                            }
                            intentJoinQuiz.putExtra("Student",id);
                            startActivity(intentJoinQuiz);
                        }
                        else if ( authenticate(usernameString, passwordString) ==2) {
                            Toast.makeText(MainActivity.this, "TEACHER LOGIN-ED", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        //incorrect
                        Toast.makeText(MainActivity.this, "No such username", Toast.LENGTH_SHORT).show();
                }

            }


        });
        TextView createAccountRedirect = findViewById(R.id.createAccountText);
        createAccountRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }

        });

    }
    public boolean checkUsernameInDB(String username) {
        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE username = ?", new String[] { username });
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
       else {
           cursor = db.rawQuery("SELECT * FROM teachers WHERE username = ?", new String[]{username});
            if (cursor.getCount() > 0){
                cursor.close();
                return true;
            }
       }
        cursor.close();
           return false;
    }

    public int authenticate(String username, String password) {

            Cursor cursor = db.rawQuery("SELECT password FROM students WHERE username = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                String passwordFromDatabase = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                Toast.makeText(MainActivity.this, passwordFromDatabase, Toast.LENGTH_SHORT).show();
                if (passwordFromDatabase.equals(password)) {
                    return 1;
                }
            }
            cursor.close();

            cursor = db.rawQuery("SELECT password FROM teachers WHERE username = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                String passwordFromDatabase = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                if (passwordFromDatabase.equals(username)) {
                    return 2;
                }
            }


        return 0;
    }
}