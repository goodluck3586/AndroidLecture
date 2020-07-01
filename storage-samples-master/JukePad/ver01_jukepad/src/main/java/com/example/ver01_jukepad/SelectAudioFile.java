package com.example.ver01_jukepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectAudioFile extends AppCompatActivity {

    TextView textViewTitle;
    EditText editTextKeyword;
    Button btnSelect, btnSearch;
    ListView listView;
    ArrayList<HashMap<String, String>> audioDataList;
    ArrayList<String> arrayList;
    MediaPlayer mediaPlayer;

    //region 체크할 권한 목록
    String[] permissionList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_audio_file);

        //region 참조변수에 객체 연결
        textViewTitle = findViewById(R.id.textViewSelectAudioTitle);
        editTextKeyword = findViewById(R.id.editTextSelectAudioKdyword);
        btnSearch = findViewById(R.id.btnSearch);
        btnSelect = findViewById(R.id.btnAdd_selectAudio);
        listView = findViewById(R.id.listview_select_audiofile);
        //endregion

        // permissionList의 권한 요청
        ActivityCompat.requestPermissions(this, permissionList, 0);

        audioDataList = readMediaStoreAudioFile();

//        String[] keys = {"name", "title"};
//        int[] ids = {android.R.id.text1, android.R.id.text2};
//        SimpleAdapter adapter = new SimpleAdapter(this, audioDataList, android.R.layout.simple_list_item_2, keys, ids);

        arrayList = readMediaStoreAudioFileName();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, arrayList);

        // 라디오단추 선택모드 세팅
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setAdapter(adapter);

        //region 오디오 파일 선택 -> Toast 띄우기
        /*istView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SelectAudioFile.this, arrayList.get(listView.getCheckedItemPosition()), Toast.LENGTH_SHORT).show();
            }
        });*/
        //endregion

        //region 오디오 파일을 선택 -> 재생
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(audioDataList.get(position).get("id")));
                if(mediaPlayer!=null)
                    mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mediaPlayer.setDataSource(getApplicationContext(), contentUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
        //endregion

        // SELECT 버튼 클릭 이벤트 처리
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaySoundActivity.class);
                // category_name, button_name, audio_id, audio_filename
                intent.putExtra("DISPLAY_NAME", arrayList.get(listView.getCheckedItemPosition()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private ArrayList<String> readMediaStoreAudioFileName(){
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor = SelectAudioFile.this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME},
                null,
                null,
                MediaStore.Audio.Media.DISPLAY_NAME + " ASC");

        // cursor에 검색된 MediaStore.Audio 데이터가 없으면 return
        if (cursor == null || !cursor.moveToFirst()) {
            Log.e("MediaStoreError", "cursor null or cursor is empty");
            return null;
        }

        if (cursor != null) {
            int cursorCount = cursor.getColumnCount();
            String[] columnNames = cursor.getColumnNames();
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(0));
            }
            cursor.close();
        }

        return arrayList;
    }

    private ArrayList<HashMap<String, String>> readMediaStoreAudioFile() {
        ArrayList<HashMap<String, String>> audioList = new ArrayList<HashMap<String, String>>();
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

        String selection = MediaStore.Audio.Media.DISPLAY_NAME + " like ? ";
        String[] selectionArgs = new String[]{"%아이유%"};
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

        /**
         실질적으로 쿼리하는 코드
         Uri: 찾고자하는 데이터의 Uri
         Projection: DB의 column과 같다. 결과로 받고 싶은 데이터의 종류
         Selection: DB의 where 키워드와 같다. 어떤 조건으로 필터링된 결과를 받을 때 사용
         Selection args: Selection과 함께 사용
         Sort order: 쿼리 결과 데이터를 sorting할 때 사용

         위의 조건으로 쿼리한 데이터가 있다면 Cursor로 결과를 순회(Loop)할 수 있다.
         */
        try{
            Cursor cursor = SelectAudioFile.this.getContentResolver().query(
                    externalUri,
                    projection,
                    null,
                    null,
                    sortOrder);

            // MediaStore.Audio 데이터가 없으면 return
            if (cursor == null || !cursor.moveToFirst()) {
                Log.e("cursor data", "cursor null or cursor is empty");
                return null;
            }

            if (cursor != null) {
                int cursorCount = cursor.getColumnCount();
                String[] columnNames = cursor.getColumnNames();
                while (cursor.moveToNext()) {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("id", cursor.getString(0));
                    hashMap.put("name", cursor.getString(1));
                    hashMap.put("mime_type", cursor.getString(2));
                    hashMap.put("album", cursor.getString(3));
                    hashMap.put("artist", cursor.getString(4));
                    hashMap.put("title", cursor.getString(5));
                    hashMap.put("data", cursor.getString(6));

                    audioList.add(hashMap);
                    //Log.d("cursor data", "MediaStore.Audio.Media._ID : " + cursor.getString(0));
                }
                cursor.close();
            }
        }catch (Exception e){
            Log.d("CursorError", e.toString());
        }
        return audioList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
            mediaPlayer.release();
    }

    // 권한 요청 후 호출되는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0 && grantResults.length > 0){

            String strGrantedList="", strDeniedList="";
            for(int i=0; i<permissions.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    strGrantedList += permissions[i]+'\n';
                }else if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    strDeniedList += permissions[i]+'\n';
                }
            }
            Toast.makeText(SelectAudioFile.this, strGrantedList, Toast.LENGTH_SHORT).show();
        }
    }
}
