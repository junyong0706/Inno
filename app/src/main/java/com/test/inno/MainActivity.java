package com.test.inno;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{
    private final int REQUEST_BLUETOOTH_ENABLE = 100;
    InputMethodManager manager;

    private ViewGroup rootView;
    private EditText ed_1,ed_2,ed_3,ed_4,ed_5,ed_6,ed_7,ed_8,ed_9,
            ed_10,ed_11,ed_12,ed_13,ed_14,ed_15,ed_16;
    private Button btn_mode,btn_vol_up,btn_vol_down,btn_speed_up,btn_speed_down,
    btn_num_1,btn_num_2,btn_num_3,btn_num_4,btn_num_5,btn_num_6,btn_num_7,btn_num_8,btn_num_9,btn_num_0,
    btn_all_delete,btn_delete;

    private ImageButton btn_play,btn_pause,btn_stop;
    private TextView txt_vol,txt_speed;

    private LinearLayout layout_num_key;

    private boolean modeflag = true;

    private int vol = 5;
    private BigDecimal speed = new BigDecimal("1.0");
    private DecimalFormat df;
    private BigDecimal decimal_plus = new BigDecimal("0.1");
    private BigDecimal decimal_minus = new BigDecimal("-0.1");
    ConnectedTask mConnectedTask = null;
    static BluetoothAdapter mBluetoothAdapter;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    static boolean isConnectionError = false;
    private static final String TAG = "BluetoothClient";

    @Override
    @SuppressLint("MissingPermission")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Log.d(TAG, "Initalizing Bluetooth adapter...");
        init();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        } else {
            Log.d(TAG, "Initialisation successful.");
            //연결할 블루투스 목록 보여주기
            //showPairedDevicesListDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mConnectedTask != null) {

            mConnectedTask.cancel(true);
        }
    }

    private void init(){
        manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        rootView = findViewById(android.R.id.content);  // 현재 Activity의 최상위 ViewGroup 가져오기

        df = new DecimalFormat("#.#");

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

        txt_vol = findViewById(R.id.txt_vol);
        txt_speed = findViewById(R.id.txt_speed);

        btn_mode = findViewById(R.id.btn_mode);
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
        btn_all_delete = findViewById(R.id.btn_all_delete);
        btn_play = findViewById(R.id.btn_play);
        btn_pause = findViewById(R.id.btn_pause);
        btn_stop= findViewById(R.id.btn_stop);
        btn_vol_up = findViewById(R.id.btn_vol_up);
        btn_vol_down = findViewById(R.id.btn_vol_down);
        btn_speed_up = findViewById(R.id.btn_speed_up);
        btn_speed_down = findViewById(R.id.btn_speed_down);

        layout_num_key = findViewById(R.id.layout_num_key);

        ed_1.setOnTouchListener(this);
        ed_2.setOnTouchListener(this);
        ed_3.setOnTouchListener(this);
        ed_4.setOnTouchListener(this);
        ed_5.setOnTouchListener(this);
        ed_6.setOnTouchListener(this);
        ed_7.setOnTouchListener(this);
        ed_8.setOnTouchListener(this);
        ed_9.setOnTouchListener(this);
        ed_10.setOnTouchListener(this);
        ed_11.setOnTouchListener(this);
        ed_12.setOnTouchListener(this);
        ed_13.setOnTouchListener(this);
        ed_14.setOnTouchListener(this);
        ed_15.setOnTouchListener(this);
        ed_16.setOnTouchListener(this);

        // Button Views
        btn_mode.setOnClickListener(this);
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
        btn_all_delete.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_vol_up.setOnClickListener(this);
        btn_vol_down.setOnClickListener(this);
        btn_speed_up.setOnClickListener(this);
        btn_speed_down.setOnClickListener(this);

        rootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (newFocus != null) {
                    // 새로운 뷰가 포커스를 받았을 때
                    Log.d(TAG, "New focused view ID: " + newFocus.getId());
                    Log.d(TAG, "New focused view ID: " + newFocus.getClass().getName());
                    hideKeyboard(newFocus);
                }
            }
        });

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (v instanceof EditText){
                if (modeflag){
                    //수정모드일때
                    Log.d(TAG,"modeflag = " + modeflag);
                    v.requestFocus();
                    hideKeyboard(v);
                    return true;
                }else {
                    //재생모드일때
                    Log.d(TAG,"modeflag = " + modeflag);
                    sendMessage(((EditText) v).getText().toString());
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        View focusedView = getCurrentFocus();
        EditText focusedEditText = (EditText) focusedView;

        if (id == R.id.btn_mode) {
            // btn_mode 클릭 시 동작
            if (modeflag){
                if (focusedView != null){
                    focusedView.clearFocus();
                }
                setViewsVisible();
                sendMessage("vol_" + vol);
                sendMessage("spd_" + speed);
                modeflag = false;
                btn_mode.setText("재생모드");
                ((TextView)findViewById(R.id.txt_mode)).setText("현재 모드 : 재생모드");
            }else {
                setViewsGone();
                modeflag = true;
                btn_mode.setText("수정모드");
                ((TextView)findViewById(R.id.txt_mode)).setText("현재 모드 : 수정모드");
            }
        } else if (id == R.id.btn_num_0) {
            // btn_num_0 클릭 시 동작
            focusedEditText.append("0");
        } else if (id == R.id.btn_num_1) {
            // btn_num_1 클릭 시 동작
            focusedEditText.append("1");
        } else if (id == R.id.btn_num_2) {
            // btn_num_2 클릭 시 동작
            focusedEditText.append("2");
        } else if (id == R.id.btn_num_3) {
            // btn_num_3 클릭 시 동작
            focusedEditText.append("3");
        } else if (id == R.id.btn_num_4) {
            // btn_num_4 클릭 시 동작
            focusedEditText.append("4");
        } else if (id == R.id.btn_num_5) {
            // btn_num_5 클릭 시 동작
            focusedEditText.append("5");
        } else if (id == R.id.btn_num_6) {
            // btn_num_6 클릭 시 동작
            focusedEditText.append("6");
        } else if (id == R.id.btn_num_7) {
            // btn_num_7 클릭 시 동작
            focusedEditText.append("7");
        } else if (id == R.id.btn_num_8) {
            // btn_num_8 클릭 시 동작
            focusedEditText.append("8");
        } else if (id == R.id.btn_num_9) {
            // btn_num_9 클릭 시 동작
            focusedEditText.append("9");
        } else if (id == R.id.btn_delete) {
            // btn_delete 클릭 시 동작
            String currentText = focusedEditText.getText().toString();
            if (currentText.length() > 0) {
                focusedEditText.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (id == R.id.btn_all_delete) {
            // btn_all_delete 클릭 시 동작
            focusedEditText.setText("");
        } else if (id == R.id.btn_play) {
            // btn_play 클릭 시 동작
            sendMessage("unpause");
        } else if (id == R.id.btn_pause) {
            // btn_pause 클릭 시 동작
            sendMessage("pause");
        } else if (id == R.id.btn_stop) {
            // btn_stop 클릭 시 동작
            sendMessage("stop");
        } else if (id == R.id.btn_vol_up) {
            // btn_vol_up 클릭 시 동작
            if (vol == 10){
                Toast.makeText(this," 이미 최대 볼륨입니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            vol++;
            txt_vol.setText(vol+"");
            sendMessage("vol_"+vol);
        } else if (id == R.id.btn_vol_down) {
            // btn_vol_down 클릭 시 동작
            if (vol == 0){
                Toast.makeText(this," 이미 최소 볼륨입니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            vol--;
            txt_vol.setText(vol+"");
            sendMessage("vol_"+vol);
        } else if (id == R.id.btn_speed_up) {
            // btn_speed_up 클릭 시 동작
            if (txt_speed.getText().toString().equals("x2.0")){
                Toast.makeText(this," 이미 최대 재생속도입니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            speed = speed.add(decimal_plus);
            txt_speed.setText("x"+speed);
            sendMessage("spd_"+speed);
        } else if (id == R.id.btn_speed_down) {
            // btn_speed_down 클릭 시 동작
            if (txt_speed.getText().toString().equals("x0.5")){
                Toast.makeText(this," 이미 최소 재생속도입니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            speed = speed.add(decimal_minus);
            txt_speed.setText("x"+speed);
            sendMessage("spd_"+speed);
        }
    }
    //상단버튼3개 및 숫자키영역 가려주는 함수
    private void setViewsGone() {
        btn_play.setVisibility(View.INVISIBLE);
        btn_pause.setVisibility(View.INVISIBLE);
        btn_stop.setVisibility(View.INVISIBLE);

        layout_num_key.setVisibility(View.VISIBLE);

        /*btn_vol_up.setEnabled(false);
        btn_vol_down.setEnabled(false);
        btn_speed_up.setEnabled(false);
        btn_speed_down.setEnabled(false);*/

    }
    //상단버튼3개 및 숫자키영역 보여주는 함수
    private void setViewsVisible() {
        btn_play.setVisibility(View.VISIBLE);
        btn_pause.setVisibility(View.VISIBLE);
        btn_stop.setVisibility(View.VISIBLE);

        layout_num_key.setVisibility(View.INVISIBLE);

        /*btn_vol_up.setEnabled(true);
        btn_vol_down.setEnabled(true);
        btn_speed_up.setEnabled(true);
        btn_speed_down.setEnabled(true);*/
    }

    //키보드 가려주는 함수
    public void hideKeyboard(View view)
    {
        try {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;

        @SuppressLint("MissingPermission")
        ConnectTask(BluetoothDevice bluetoothDevice) {
            mBluetoothDevice = bluetoothDevice;
            mConnectedDeviceName = bluetoothDevice.getName();

            //SPP
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                Log.d(TAG, "create socket for " + mConnectedDeviceName);

            } catch (IOException e) {
                Log.e(TAG, "socket create failed " + e.getMessage());
            }

        }


        @Override
        @SuppressLint("MissingPermission")
        protected Boolean doInBackground(Void... params) {

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mBluetoothSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " +
                            " socket during connection failure", e2);
                }

                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean isSucess) {

            if (isSucess) {
                connected(mBluetoothSocket);
            } else {

                isConnectionError = true;
                Log.d(TAG, "Unable to connect device");
                showErrorDialog("Unable to connect device");
            }
        }
    }


    public void connected(BluetoothSocket socket) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
    }


    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private BluetoothSocket mBluetoothSocket = null;

        ConnectedTask(BluetoothSocket socket) {

            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "socket not created", e);
            }

            Log.d(TAG, "connected to " + mConnectedDeviceName);
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            byte[] readBuffer = new byte[1024];
            int readBufferPosition = 0;


            while (true) {

                if (isCancelled()) return false;

                try {

                    int bytesAvailable = mInputStream.available();

                    if (bytesAvailable > 0) {

                        byte[] packetBytes = new byte[bytesAvailable];

                        mInputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {

                            byte b = packetBytes[i];
                            if (b == '\n') {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                String recvMessage = new String(encodedBytes, "UTF-8");

                                readBufferPosition = 0;

                                Log.d(TAG, "recv message: " + recvMessage);
                                publishProgress(recvMessage);
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException e) {

                    Log.e(TAG, "disconnected", e);
                    return false;
                }
            }

        }

        @Override
        protected void onProgressUpdate(String... recvMessage) {

            mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);
        }

        @Override
        protected void onPostExecute(Boolean isSucess) {
            super.onPostExecute(isSucess);

            if (!isSucess) {


                closeSocket();
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                showErrorDialog("Device connection was lost");
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);

            closeSocket();
        }

        void closeSocket() {

            try {

                mBluetoothSocket.close();
                Log.d(TAG, "close socket()");

            } catch (IOException e2) {

                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }

        void write(String msg) {

            msg += "\n";

            try {
                mOutputStream.write(msg.getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception during send", e);
            }

        }
    }

    @SuppressLint("MissingPermission")
    public void showPairedDevicesListDialog() {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);

        if ( pairedDevices.length == 0 ){
            showQuitDialog( "No devices have been paired.\n"
                    +"You must pair it with another device.");
            return;
        }

        String[] items;
        items = new String[pairedDevices.length];
        for (int i=0;i<pairedDevices.length;i++) {
            items[i] = pairedDevices[i].getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select device");
        builder.setCancelable(false);
        //목록에서 누른 아이템을 연결요청
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ConnectTask task = new ConnectTask(pairedDevices[which]);
                task.execute();
            }
        });
        builder.create().show();
    }

    public void showErrorDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if ( isConnectionError  ) {
                    isConnectionError = false;
                    finish();
                }
            }
        });
        builder.create().show();
    }


    public void showQuitDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    void sendMessage(String msg){
        Log.d(TAG, "send message: " + msg);
        if ( mConnectedTask != null ) {
            mConnectedTask.write(msg);
            mConversationArrayAdapter.insert("Me:  " + msg, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
            if (resultCode == RESULT_OK) {
                //BlueTooth is now Enabled
                showPairedDevicesListDialog();
            }
            if (resultCode == RESULT_CANCELED) {
                showQuitDialog("You need to enable bluetooth");
            }
        }
    }
}