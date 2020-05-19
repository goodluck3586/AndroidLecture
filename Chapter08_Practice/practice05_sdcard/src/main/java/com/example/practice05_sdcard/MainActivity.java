package com.example.practice05_sdcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnRead, btnWrite;
    EditText editText;
    String[] permissionList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String path;    // 외부 저장소 까지의 경로를 저장할 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);
        editText = findViewById(R.id.editText);

        /*// 권한 확인(PERMISSION_GRANTED 또는 PERMISSION_DENIED 반환)
        for(String permission : permissionList){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, permission+"권한 없음", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, permission+"권한 있음", Toast.LENGTH_SHORT).show();
            }
        }*/

        // 저장소 권한을 요청
        ActivityCompat.requestPermissions(this, permissionList, 0);

        // 저장소 사용 가능 여부 체크
        isExternalStorageReadable();
        isExternalStorageWritable();

        getPrivateDocumentsStorageDir(getApplicationContext(), "testFile");

////        공개 디렉터리에 저장(다른 앱에서 액세스할 수 있는 외부 저장소에 파일을 저장하려면 다음 API 중 하나를 사용하세요.)
//          https://developer.android.com/training/data-storage/files/external#java
//          사진이나 오디오 파일, 동영상을 저장하는 경우 MediaStore API를 사용합니다.
//          기타 다른 파일(예: PDF 문서)을 저장하는 경우 저장소 액세스 프레임워크의 일부인 ACTION_CREATE_DOCUMENT 인텐트를 사용합니다.

/*//        비공개 디렉터리에 저장
        앱에 비공개인 파일을 외부 저장소에 저장하려면 getExternalFilesDir()를 호출하고
        원하는 디렉터리 유형을 나타내는 이름을 전달하여 앱별 디렉터리를 획득할 수 있다.
        이 방식으로 만들어진 각 디렉터리는 앱의 모든 외부 저장소 파일을 캡슐화하는 상위 디렉터리에 추가되며
        이러한 파일은 사용자가 앱을 제거할 때 시스템에서 정리됩니다.*/

        // 외부 저장소까지의 경로 구하기
        File file = Environment.getExternalStorageDirectory();
        final String absolutePath = file.getAbsolutePath();

        // 패키지명 구하기
        String packagePath = getPackageName();

        // 전체 경로
        path = absolutePath + "/android/data/" + packagePath;

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "hello world!!!";

                if(isExternalStorageReadable())
                    Toast.makeText(MainActivity.this, "외부 저장장치 연결됨.", Toast.LENGTH_SHORT).show();

//                File f1 = getExternalFilesDir();
//                File file = new File(path+"testFile.txt");
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                try {
                    FileOutputStream fos = new FileOutputStream("/sdcard/sd_txt.txt");
                    fos.write(data.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        /*btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExternalDirectory();

                try(FileOutputStream fos = new FileOutputStream(path + "text.txt")) {
                    fos.write(editText.getText().toString().getBytes());
                    Toast.makeText(MainActivity.this, "외부 저장소 쓰기 완료", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

        /*btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try(FileInputStream fs = new FileInputStream(path)) {
                    byte[] byteArray = new byte[fs.available()];
                    fs.read(byteArray);
                    editText.setText(new String(byteArray));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getPrivateDocumentsStorageDir(Context context, String documentsName) {
        // Get the directory for the app's private documents directory.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), documentsName);
        if (!file.mkdirs()) {
            Toast.makeText(context, "Directory not created", Toast.LENGTH_SHORT).show();
        }
        return file;
    }


    // 외부 저장소 경로를 있는지 확인하고, 없으면 생성한다.
    public void checkExternalDirectory(){
        File file = new File(path);
        if(file.exists() == false){
            boolean mkdirSuccessed = file.mkdir();
            if(mkdirSuccessed)
                Log.d("mkdirSuccessed", String.valueOf(mkdirSuccessed));
        }
    }
}
