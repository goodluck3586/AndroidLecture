package com.example.testnum2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {
    Button execute;
    RadioGroup rg;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        execute=findViewById(R.id.execute);
        rg=findViewById(R.id.radiogruop);
        text=findViewById(R.id.textView);
        final Intent intent = new Intent();


        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("select",rg.getCheckedRadioButtonId());
                finish();
            }
        });
    }
}
