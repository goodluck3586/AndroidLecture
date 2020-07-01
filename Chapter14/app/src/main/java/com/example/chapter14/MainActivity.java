package com.example.chapter14;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    Button btnPass, btnStop, btnDistance, btnMask;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region 참조변수에 객체 할당
        imageView = findViewById(R.id.imageView);
        btnPass = findViewById(R.id.btnPass);
        btnStop = findViewById(R.id.btnStop);
        btnDistance = findViewById(R.id.btnDistance);
        btnMask = findViewById(R.id.btnMask);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.KOREAN);
                }
            }
        });
        //endregion

        btnPass.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnDistance.setOnClickListener(this);
        btnMask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPass:
                imageView.setImageResource(R.drawable.good2);
                textToSpeech.speak("통과", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.btnStop:
                imageView.setImageResource(R.drawable.omg);
                textToSpeech.speak("발열 있음", TextToSpeech.QUEUE_FLUSH, null);
            break;
            case R.id.btnDistance:
                imageView.setImageResource(R.drawable.social_distancing);
                textToSpeech.speak("사회적 거리두기를 지켜주세요", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.btnMask:
                imageView.setImageResource(R.drawable.mask);
                textToSpeech.speak("마스크를 써주세요", TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }
}
