package com.example.testnum1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button plus,minus,multi,div;
    EditText editText1,editText2;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("2206노승균");

        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        multi=findViewById(R.id.multi);
        div=findViewById(R.id.div);
        editText1=findViewById(R.id.edit1);
        editText2=findViewById(R.id.edit2);
        textView=findViewById(R.id.textView);

        plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a= Integer.parseInt(String.valueOf(editText1.getText()));
                int b= Integer.parseInt(String.valueOf(editText2.getText()));
                textView.setText("계산 결과 : "+(a+b));

            }
        });
        minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a= Integer.parseInt(String.valueOf(editText1.getText()));
                int b= Integer.parseInt(String.valueOf(editText2.getText()));
                textView.setText("계산 결과 : "+(a-b));

            }
        });
        multi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a= Integer.parseInt(String.valueOf(editText1.getText()));
                int b= Integer.parseInt(String.valueOf(editText2.getText()));
                textView.setText("계산 결과 : "+(a*b));

            }
        });
        div.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a= Integer.parseInt(String.valueOf(editText1.getText()));
                int b= Integer.parseInt(String.valueOf(editText2.getText()));
                textView.setText("계산 결과 : "+((a*1.0f)/b));

            }
        });

    }
}
