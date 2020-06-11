package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button plus, minus, x, slash;
    TextView result;
    EditText num1, num2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("2017 신이삭");
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        x = findViewById(R.id.x);
        slash = findViewById(R.id.slash);

        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        result = findViewById(R.id.result);
        final int n1 = Integer.parseInt(num1.getText().toString());
        final int n2 = Integer.parseInt(num2.getText().toString());
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("계산결과 : " +(n1 + n2));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("계산결과 : " + (n1 - n2));
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("계산결과 : " + (n1 * n2));
            }
        });
        slash.setOnClickListener(new View.OnClickListener() {
            double n1 = Double.parseDouble(num1.getText().toString());
            double n2 = Double.parseDouble(num2.getText().toString());
            @Override
            public void onClick(View v) {
                result.setText("계산결과 : " + (n1 / n2));
            }
        });
    }
}
