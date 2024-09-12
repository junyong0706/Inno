package com.test.inno;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewBluetoothActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "FileTransfer";
    private TextView currentBlinkingTextView = null;
    private AlphaAnimation blinkAnimation;
    private TextView txtMode, ed_1, ed_2, ed_3, ed_4, ed_5, ed_6, ed_7, ed_8, ed_9, ed_10,
            ed_11, ed_12, ed_13, ed_14, ed_15, ed_16;

    private Button btn_mode,btn_vol_up,btn_vol_down,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,
            btn_num_1,btn_num_2,btn_num_3,btn_num_4,btn_num_5,btn_num_6,btn_num_7,btn_num_8,btn_num_9,btn_num_0,
            btn_delete;

    private boolean playflag = false;
    private boolean mode_flag = false;
    private int index_mp3 = 0;
    private MediaPlayer mediaPlayer;

    private ArrayList<File> fileArrayList;

    String path = Environment.getExternalStoragePublicDirectory("DIRECTORY_MUSIC").getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_bluetooth);
        checkAndRequestPermissions();
        copyFilesFromUsb();
        initBlinkAnimation();
        init();
    }
    private void init(){

        fileArrayList = new ArrayList<>();

        ed_1 = findViewById(R.id.ed_1);
        ed_2 = findViewById(R.id.ed_2);
        ed_3 = findViewById(R.id.ed_3);
        ed_4 = findViewById(R.id.ed_4);
        ed_5 = findViewById(R.id.ed_5);
        ed_6 = findViewById(R.id.ed_6);
        ed_7 = findViewById(R.id.ed_7);
        ed_8 = findViewById(R.id.ed_8);
        ed_9 = findViewById(R.id.ed_9);
        ed_10 = findViewById(R.id.ed_10);
        ed_11 = findViewById(R.id.ed_11);
        ed_12 = findViewById(R.id.ed_12);
        ed_13 = findViewById(R.id.ed_13);
        ed_14 = findViewById(R.id.ed_14);
        ed_15 = findViewById(R.id.ed_15);
        ed_16 = findViewById(R.id.ed_16);

        btn_num_0 = findViewById(R.id.btn_num_0);
        btn_num_1 = findViewById(R.id.btn_num_1);
        btn_num_2 = findViewById(R.id.btn_num_2);
        btn_num_3 = findViewById(R.id.btn_num_3);
        btn_num_4 = findViewById(R.id.btn_num_4);
        btn_num_5 = findViewById(R.id.btn_num_5);
        btn_num_6 = findViewById(R.id.btn_num_6);
        btn_num_7 = findViewById(R.id.btn_num_7);
        btn_num_8 = findViewById(R.id.btn_num_8);
        btn_num_9 = findViewById(R.id.btn_num_9);
        btn_delete = findViewById(R.id.btn_delete);
        btn_mode = findViewById(R.id.btn_mode);
        btn_vol_up = findViewById(R.id.btn_vol_up);
        btn_vol_down = findViewById(R.id.btn_vol_down);


        ed_1.setOnClickListener(this);
        ed_2.setOnClickListener(this);
        ed_3.setOnClickListener(this);
        ed_4.setOnClickListener(this);
        ed_5.setOnClickListener(this);
        ed_6.setOnClickListener(this);
        ed_7.setOnClickListener(this);
        ed_8.setOnClickListener(this);
        ed_9.setOnClickListener(this);
        ed_10.setOnClickListener(this);
        ed_11.setOnClickListener(this);
        ed_12.setOnClickListener(this);
        ed_13.setOnClickListener(this);
        ed_14.setOnClickListener(this);
        ed_15.setOnClickListener(this);
        ed_16.setOnClickListener(this);

        // Button Views
        btn_num_0.setOnClickListener(this);
        btn_num_1.setOnClickListener(this);
        btn_num_2.setOnClickListener(this);
        btn_num_3.setOnClickListener(this);
        btn_num_4.setOnClickListener(this);
        btn_num_5.setOnClickListener(this);
        btn_num_6.setOnClickListener(this);
        btn_num_7.setOnClickListener(this);
        btn_num_8.setOnClickListener(this);
        btn_num_9.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_mode.setOnClickListener(this);
        btn_vol_up.setOnClickListener(this);
        btn_vol_down.setOnClickListener(this);
        
    }
    public void playMp3Files(List<File> mp3Files) {
        Log.d("Mp3Player", "시작");
        if (mp3Files == null || mp3Files.isEmpty()) {
            Log.d("Mp3Player", "MP3 파일이 없습니다.");
            return;
        }

        // 첫 번째 파일을 재생하기 위한 설정 (원하는 대로 변경 가능)
        File mp3File = mp3Files.get(index_mp3); // 첫 번째 파일 선택

        if (mp3File != null && mp3File.exists() && mp3File.getName().endsWith(".mp3")) {
            try {

                if (mediaPlayer == null) {
                    // 새로운 MediaPlayer 객체 생성
                    mediaPlayer = new MediaPlayer();
                } else {
                    mediaPlayer.reset(); // 기존 MediaPlayer를 재사용할 경우 reset() 호출
                }
                mediaPlayer.setDataSource(mp3File.getPath());
                mediaPlayer.prepare(); // 파일 준비
                mediaPlayer.start();   // 재생 시작

                Log.d("Mp3Player", "재생 중인 파일: " + mp3File.getName());

                // 재생이 끝났을 때의 콜백 설정
                mediaPlayer.setOnCompletionListener(mp -> {
                    Log.d("Mp3Player", "재생이 완료되었습니다.");
                    // 재생이 완료되면 MediaPlayer 해제
                    if (index_mp3 == 0){
                        index_mp3++;
                    }
                    // 다음 파일이 있으면 재생, 없으면 완료 메시지
                    if (index_mp3 < mp3Files.size()) {
                        playMp3Files(mp3Files); // 다음 파일 재생
                    } else {
                        Log.d("Mp3Player", "모든 MP3 파일을 재생했습니다.");
                        mediaPlayer.release(); // 모든 재생이 완료되면 MediaPlayer 해제
                        mediaPlayer = null;
                    }
                });

            } catch (IOException e) {
                Log.e("Mp3Player", "MP3 파일 재생 중 오류 발생: " + e.getMessage());
            }
        } else {
            Log.d("Mp3Player", "MP3 파일이 아닙니다: " + mp3File.getName());
        }
    }
    private void initBlinkAnimation() {
        blinkAnimation = new AlphaAnimation(1.0f, 0.5f);  // 1.0 -> 0.0 : 완전 불투명 -> 투명
        blinkAnimation.setDuration(500);  // 500ms 동안 애니메이션 실행
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);
    }
    private void handleTextViewClick(TextView textView) {
        // 이전에 선택된 텍스트뷰가 있으면 애니메이션을 중지
        if (currentBlinkingTextView != null) {
            currentBlinkingTextView.clearAnimation();
        }

        // 현재 클릭한 텍스트뷰에 애니메이션 적용
        currentBlinkingTextView = textView;
        if (!mode_flag){
            currentBlinkingTextView.startAnimation(blinkAnimation);
        }
    }


    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 이상에서 MANAGE_EXTERNAL_STORAGE 권한 확인
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 200);
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 200);
                }
                return false;
            }
        } else {
            // Android 10 이하에서 권한 확인
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // 권한 요청
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 200);
                return false;
            }
        }
        return true;
    }

    public ArrayList<File> getFilesStartingWith(String prefix) {
        Log.d("FileSearch", "prefix = " +prefix);
        //리스트 가져올때마다 비워주기
        fileArrayList.clear();
        // MUSIC 디렉토리 경로 가져오기
        File musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        // 결과를 담을 리스트
        ArrayList<File> matchingFiles = new ArrayList<>();

        // 음악 폴더가 존재하고, 폴더일 경우에만 진행
        if (musicFolder.exists() && musicFolder.isDirectory()) {
            File[] files = musicFolder.listFiles();

            if (files != null) {
                for (File file : files) {
                    // 파일 이름이 prefix로 시작하는지 확인
                    if (file.isFile() && file.getName().startsWith(prefix)) {
                        Log.d("FileSearch", "파일명 = " + file.getName());
                        matchingFiles.add(file);
                    }
                }
            }
        } else {
            Log.d("FileSearch", "MUSIC 폴더를 찾을 수 없습니다.");
        }

        return matchingFiles;
    }
    private void copyFilesFromUsb() {
        // USB에서 파일 경로 지정 (USB 경로는 실제 USB 장치가 연결된 후 경로를 찾아야 함)
        String usbFolderPath = getExUSBPath(this) + "/CCM";  // USB의 CCM 폴더 경로
        File usbFolder = new File(usbFolderPath);

        // 외부 저장소의 MUSIC 디렉토리 경로
        String musicFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
        File musicFolder = new File(musicFolderPath);

        // USB의 CCM 폴더가 존재하고, 폴더일 경우에만 복사 진행
        if (usbFolder.exists() && usbFolder.isDirectory()) {
            File[] files = usbFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // 파일 복사 실행
                        try {
                            //맥에서 복사했을때 처리
                            if (!file.getName().startsWith("._")){
                                Log.d("FILE","파일이름 = " + file.getName());
                                copyFile(file, new File(musicFolder, file.getName()));
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                Toast.makeText(this, "파일 복사 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "CCM 폴더에 파일이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "USB CCM 폴더를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyFile(File src, File dst) throws IOException {
        try (FileChannel inChannel = new FileInputStream(src).getChannel();
             FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
    }
    public static String getExUSBPath(Context mContext) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        Class<?> diskClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.VolumeInfo");
            diskClazz=Class.forName("android.os.storage.DiskInfo");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumes");
            Method getDiskMethod = storageVolumeClazz.getMethod("getDisk");
            Method getFileMethod = storageVolumeClazz.getMethod("getPath");
            Method isUSbMethod =diskClazz.getMethod("isUsb");
            List<Object> result = (List<Object>) getVolumeList.invoke(mStorageManager);
            final int length =result.size();
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = result.get(i);
                File file = (File) getFileMethod.invoke(storageVolumeElement);
                Object diskObject=getDiskMethod.invoke(storageVolumeElement);
                Boolean isUsb=false;
                if(diskObject!=null)isUsb=(Boolean)isUSbMethod.invoke(diskObject);
                //Log.d("sd", "File:"+file.getAbsolutePath()+";isSd:"+isSd);
                if(isUsb)return file.getAbsolutePath();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인되었으면 파일 복사 실행
                copyFilesFromUsb();
            } else {
                Toast.makeText(this, "저장소 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("포커스","타입 - " + v.getClass());
        if (v.getClass().getName().equals("androidx.appcompat.widget.AppCompatTextView")){
            Log.d("포커스","타입 - 텍스트뷰");
            v.requestFocus();
            handleTextViewClick((TextView) v);
        }
        //클릭한 텍스트뷰에 애니메이션넣기
        int id = v.getId();
        if (id == R.id.txt_mode) {
            // txtMode 클릭 시 동작
        } else if (id == R.id.ed_1) {
            // ed1 클릭 시 동작
        } else if (id == R.id.ed_2) {
            // ed2 클릭 시 동작
        } else if (id == R.id.ed_3) {
            // ed3 클릭 시 동작
        } else if (id == R.id.ed_4) {
            // ed4 클릭 시 동작
        } else if (id == R.id.ed_5) {
            // ed5 클릭 시 동작
        } else if (id == R.id.ed_6) {
            // ed6 클릭 시 동작
        } else if (id == R.id.ed_7) {
            // ed7 클릭 시 동작
            if (mode_flag){
                String num = ed_7.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        } else if (id == R.id.ed_8) {
            // ed8 클릭 시 동작
            if (mode_flag){
                String num = ed_8.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        } else if (id == R.id.ed_9) {
            // ed9 클릭 시 동작
            if (mode_flag){
                String num = ed_9.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        } else if (id == R.id.ed_10) {
            // ed10 클릭 시 동작
            if (mode_flag){
                String num = ed_10.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        } else if (id == R.id.ed_11) {
            // ed11 클릭 시 동작
            if (mode_flag){
                String num = ed_11.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }

        } else if (id == R.id.ed_12) {
            // ed12 클릭 시 동작
            if (mode_flag){
                String num = ed_12.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        } else if (id == R.id.ed_13) {
            // ed13 클릭 시 동작
            if (mode_flag){
                String num = ed_13.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        } else if (id == R.id.ed_14) {
            // ed14 클릭 시 동작
            String num = ed_14.getText().toString();
            fileArrayList = getFilesStartingWith(num);
            playMp3Files(fileArrayList);
        } else if (id == R.id.ed_15) {
            // ed15 클릭 시 동작
            String num = ed_15.getText().toString();
        } else if (id == R.id.ed_16) {
            // ed16 클릭 시 동작
            String num = ed_16.getText().toString();
        } else if (id == R.id.btn_num_0) {
            // btn_num_0 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("0");
            }
        } else if (id == R.id.btn_num_1) {
            // btn_num_1 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("1");
            }
        } else if (id == R.id.btn_num_2) {
            // btn_num_2 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("2");
            }
        } else if (id == R.id.btn_num_3) {
            // btn_num_3 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("3");
            }
        } else if (id == R.id.btn_num_4) {
            // btn_num_4 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("4");
            }
        } else if (id == R.id.btn_num_5) {
            // btn_num_5 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("5");
            }
        } else if (id == R.id.btn_num_6) {
            // btn_num_6 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("6");
            }
        } else if (id == R.id.btn_num_7) {
            // btn_num_7 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("7");
            }
        } else if (id == R.id.btn_num_8) {
            // btn_num_8 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("8");
            }
        } else if (id == R.id.btn_num_9) {
            // btn_num_9 클릭 시 동작
            if (currentBlinkingTextView != null){
                currentBlinkingTextView.append("9");
            }
        } else if (id == R.id.btn_delete) {
            // btn_delete 클릭 시 동작
            String currentText = currentBlinkingTextView.getText().toString();
            if (currentText.length() > 0) {
                currentBlinkingTextView.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (id == R.id.btn_mode) {
            // btn_mode 클릭 시 동작
            if (!mode_flag){
                mode_flag = true;
                btn_mode.setText("수정");
                if (currentBlinkingTextView != null) {
                    currentBlinkingTextView.clearAnimation();
                }
            }else {
                mode_flag = false;
                btn_mode.setText("완료");
                index_mp3 = 0;
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } else if (id == R.id.btn_1){
            if (mode_flag){
                String num = ed_1.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        }else if (id == R.id.btn_2){
            if (mode_flag){
                String num = ed_2.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        }else if (id == R.id.btn_3){
            if (mode_flag){
                String num = ed_3.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        }else if (id == R.id.btn_4){
            if (mode_flag){
                String num = ed_4.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        }else if (id == R.id.btn_5){
            if (mode_flag){
                String num = ed_5.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        }else if (id == R.id.btn_6){
            if (mode_flag){
                String num = ed_6.getText().toString();
                fileArrayList = getFilesStartingWith(num);
                playMp3Files(fileArrayList);
            }
        }

    }
}