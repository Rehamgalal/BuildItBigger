package com.example.mylibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Intent i = getIntent();
        String joke= i.getExtras().getString("joke");
        TextView textView= findViewById(R.id.recievedjoke);
        textView.setText(joke);
    }
}
