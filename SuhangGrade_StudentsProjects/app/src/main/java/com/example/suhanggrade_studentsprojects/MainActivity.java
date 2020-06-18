package com.example.suhanggrade_studentsprojects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addBtn, delBtn, editBtn;
    TextView textView;
    ListView listView;
    int selectItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> arr = new ArrayList<String>();
        arr.add("listItem 1");
        arr.add("listItem 2");
                arr.add("listItem 3");
        arr.add("listItem 4");
        arr.add("listItem 5");
                final ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arr);
                listView = findViewById(R.id.listView);
                addBtn = findViewById(R.id.addBtn);
                editBtn = findViewById(R.id.editBtn);
                delBtn = findViewById(R.id.delBtn);
                textView = findViewById(R.id.textView);
                listView.setAdapter(ad);
                listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem = position;
                textView.setText(arr.get(position));

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.add("listItem " + (arr.size() + 1));
                ad.notifyDataSetChanged();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.remove(selectItem);
                ad.notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle("리스트아이템 수정")
                        .setMessage("현재 데이터 - " + arr.get(selectItem))
                        .setView(editText)
                        .setNegativeButton("취소", null)
                        .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textView.setText(editText.getText().toString());
                                arr.set(selectItem, editText.getText().toString());
                                ad.notifyDataSetChanged();
                            }
                        })
                        .show();


            }
        });
    }
}