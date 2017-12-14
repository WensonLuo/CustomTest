package com.newpos.upos.customtext.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.newpos.upos.customtext.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClientActivity extends AppCompatActivity {
    private static final String TAG = "SocketClientActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);
        connectServer();
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(((EditText) findViewById(R.id.msg)).getText().toString());
            }
        });
    }

    private Socket mSocket;
    private BufferedWriter mWriter;
    private BufferedReader mReader;
    private static String IP = "172.21.231.164";
    private static int PORT = 2345;
    private void connectServer() {
        Log.i(TAG, "connectServer: ------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(IP,PORT);
                    mWriter = new BufferedWriter(new OutputStreamWriter(
                            mSocket.getOutputStream(),"utf-8"
                    ));
                    mReader = new BufferedReader(new InputStreamReader(
                            mSocket.getInputStream(),"utf-8"
                    ));
                    Log.i(TAG, "run: 连接服务端成功");
                } catch (IOException e) {
                    Log.i(TAG, "run: 连接服务端失败");
                    e.printStackTrace();
                    return;
                }


                try {
                    String line;
                    while ((line = mReader.readLine())!=null){
                        Log.i(TAG, "服务端消息: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "服务端：已停止服务");
                }
            }
        }).start();
    }

    private void sendMsg(String msg) {
        if (mSocket == null){
            Toast.makeText(this, "连接未完成或连接失败，无法发送消息！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mWriter.write(msg+"\n");
            mWriter.flush();
        } catch (IOException e) {
            Toast.makeText(this, "发送失败：服务端已关闭服务！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
