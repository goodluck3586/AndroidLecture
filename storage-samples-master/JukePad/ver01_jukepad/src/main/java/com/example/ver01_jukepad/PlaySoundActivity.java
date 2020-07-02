package com.example.ver01_jukepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaySoundActivity extends AppCompatActivity {

    TextView textViewTitle;
    ListView listView;
    Button btnAddSound;
    ArrayList<HashMap<String, String>> arrayList;
    SoundCustomAdapter adapter;
    String categoryName;

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);

        //region 참조변수에 객체 할당
        textViewTitle = findViewById(R.id.textView_title_playsound);
        listView = findViewById(R.id.listview_playsound);
        btnAddSound = findViewById(R.id.btnAddSound);
        arrayList = new ArrayList<HashMap<String, String>>();
        //endregion

        //region intent로 넘어온 title 문자열을 textview에 표시
        final Intent getIntent = getIntent();
        categoryName = getIntent.getStringExtra("category_name");
        textViewTitle.setText(categoryName);
        //endregion

        //region 초기에 ListView에 Button을 표시하기 위해 Database에서 sound_button 데이터를 가져와서 dataList_soundButton에 저장
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            String sql = String.format("SELECT button_name, audio_id, audio_filename FROM tb_sound_button WHERE category_name = '%s'", categoryName);
            Cursor cursor = db.rawQuery(sql, null);

            if(cursor != null){
                while (cursor.moveToNext()){
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("button_name", cursor.getString(0));
                    hm.put("audio_id", cursor.getString(1));
                    hm.put("audio_path", cursor.getString(2));
                    arrayList.add(hm);
                }
            }
        }catch (Exception e){
            Log.d("JukePadTag", e.toString());
        }finally {
            db.close();
        }
        //endregion

        adapter = new SoundCustomAdapter(this, R.layout.listview_item_category, arrayList);
        listView.setAdapter(adapter);

        //region ADD 버튼 클릭 이벤트 처리
        btnAddSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectAudioFile.class);
                intent.putExtra("category_name", categoryName);
                startActivityForResult(intent, 0);
            }
        });
        //endregion
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String audioFileName = null;
        HashMap<String, String> hashMap = null;

        if(resultCode == RESULT_OK){
            audioFileName = data.getStringExtra("DISPLAY_NAME");
            Toast.makeText(this, audioFileName, Toast.LENGTH_SHORT).show();

            hashMap = selectedAudioFile(audioFileName);

            final EditText editText = new EditText(PlaySoundActivity.this);
            final String audio_id = hashMap.get("id");
            final String audio_filepath = hashMap.get("data");

            new AlertDialog.Builder(PlaySoundActivity.this)
                .setTitle("New Category")
                .setMessage("새롭게 추가할 카테고리 이름을 입력하세요.")
                .setCancelable(false)
                .setView(editText)
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // DB에 Insert
                        try {
                            dbHelper = new DBHelper(getApplicationContext());
                            db = dbHelper.getWritableDatabase();
                            String string = "INSERT INTO tb_sound_button (category_name, button_name, audio_id, audio_filename) values ('%s', '%s', '%s', '%s')";
                            String sql = String.format(string, categoryName, editText.getText().toString(), audio_id, audio_filepath);
                            db.execSQL(sql);
                        }catch (Exception e){
                            Log.d("JukePadTag", e.toString());
                        }finally {
                            db.close();
                        }

                        // ListView에 표시
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("button_name", editText.getText().toString());
                        hm.put("audio_id", audio_id);
                        hm.put("audio_path", audio_filepath);
                        arrayList.add(hm);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
        }
    }

    // MediaStore에서 선택된 Audio File 정보를 반환하는 함수
    private HashMap<String, String> selectedAudioFile(String audioFileName) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  // MediaStore.media-type.Media.EXTERNAL_CONTENT_URI

        // media-database-columns-to-retrieve
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };

        String[] selectionArgs = new String[]{"%아이유%"};
        Cursor cursor = PlaySoundActivity.this.getContentResolver().query(
                externalUri,
                projection,
                MediaStore.Audio.Media.DISPLAY_NAME + " like ? ",   // // MediaStore.Audio.Media.DISPLAY_NAME + "=?",
                new String[]{audioFileName},
                null);

        // MediaStore.Audio 데이터가 없으면 return
        if (cursor == null || !cursor.moveToFirst()) {
            Toast.makeText(this, "cursor null or cursor is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        hashMap.put("id", cursor.getString(0));
        hashMap.put("name", cursor.getString(1));
        hashMap.put("mime_type", cursor.getString(2));
        hashMap.put("album", cursor.getString(3));
        hashMap.put("artist", cursor.getString(4));
        hashMap.put("title", cursor.getString(5));
        hashMap.put("data", cursor.getString(6));
        cursor.close();

        return hashMap;
    }
}
