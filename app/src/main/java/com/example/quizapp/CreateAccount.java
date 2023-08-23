package com.example.quizapp;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.security.InvalidKeyException;
import java.util.Locale;

public class CreateAccount extends AppCompatActivity {


    private View createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton createAccount = (MaterialButton) findViewById(R.id.createAccountButton);
        Spinner userTypeSpinner = findViewById(R.id.userType_spinner);

        createAccount.setOnClickListener(new View.OnClickListener() {
            String usernameString = "";
            String passwordString = "";
            String userType = "";
            ContentValues values = new ContentValues();

            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(CreateAccount.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                if (username.getText().toString() == null && password.getText().toString() == null) {
                    //correct
                    Toast.makeText(CreateAccount.this, "Create Account Failure", Toast.LENGTH_SHORT).show();
                } else {
                    usernameString = username.getText().toString();
                    passwordString = password.getText().toString();
                    userType = userTypeSpinner.getSelectedItem().toString().toLowerCase() + "s";
                    try {
                        dbHelper.insertData(userType, usernameString, passwordString);
                    } catch (InvalidKeyException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(CreateAccount.this, "Created Account", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}

