package com.example.myapplication;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Second extends AppCompatActivity {
    TextView textView;
    RadioButton call, camera;
    Button returnBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = findViewById(R.id.textView);
        call = findViewById(R.id.call);
        camera = findViewById(R.id.camera);
        returnBtn = findViewById(R.id.returnBtn);

        final Intent getIntent = getIntent();
        textView.setText(getIntent.getStringExtra("Maintext"));

        returnBtn.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            @Override
            public void onClick(View v) {
                if(call.isChecked()) {
                intent.putExtra("Radio", 1);
                setResult(RESULT_OK, intent);
                finish();
            } else if(camera.isChecked()) {
                intent.putExtra("Radio", 2);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        }
        });

    }
}
