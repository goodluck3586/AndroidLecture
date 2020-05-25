package com.example.ex03_practice10_18;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActicity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btnReturn = findViewById(R.id.btnReturn);

        Intent inIntent = getIntent();
        final int hap = inIntent.getIntExtra("Num1", 0) + inIntent.getIntExtra("Num2", 0);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("Hap", hap);
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }
}
