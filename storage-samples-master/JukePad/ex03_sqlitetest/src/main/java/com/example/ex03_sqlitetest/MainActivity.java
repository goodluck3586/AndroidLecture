package com.example.ex03_sqlitetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editTextCategory, editTextButtonName;
    Button btnAdd;

    DBHelper helper;    // 테이블 생성, 스키마 변경 등의 작업 처리
    SQLiteDatabase db;  // 데이터베이스에 데이터를 저장, 수정, 삭제하는 모든 SQL 질의문 처리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCategory = findViewById(R.id.editTextCategory);
        editTextButtonName = findViewById(R.id.editTextButtonName);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = editTextCategory.getText().toString();
                String button_name = editTextButtonName.getText().toString();

                try{
                    helper = new DBHelper(getApplicationContext());
                    db = helper.getWritableDatabase();
                    String sql=String.format("INSERT INTO tb_test (category, button_name) values ('%s', '%s')", category, button_name);
                    db.execSQL(sql);    // insert, update, delete 문 실행, select문은 rawQuery()로 실행
                }catch (Exception e){
                    Log.d("DatabaseError",e.toString());
                }
                finally {
                    db.close();
                }

                Intent intent = new Intent(getApplicationContext(), ReadDBActivity.class);
                startActivity(intent);
            }
        });
    }
}
