package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnFirebaseRealDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFirebaseRealDB = findViewById(R.id.btnFirebaseRealTimeDB);
        btnFirebaseRealDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                switch(v.getId()){
                    case R.id.btnFirebaseRealTimeDB:
                        i = new Intent(MainActivity.this, MemoActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}